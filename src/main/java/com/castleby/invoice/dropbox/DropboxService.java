package com.castleby.invoice.dropbox;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.castleby.invoice.component.tree.listener.StorageListener;
import com.castleby.invoice.exception.StorageException;
import com.castleby.invoice.storage.StorageEntry;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxDelta;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;

/**
 * 
 * @author taras.klym
 *
 */
@Service
public class DropboxService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DropboxService.class);

    @Value("${dropbox.accessToken}")
    private String accessToken;
    private DbxClient client;
    private List<StorageListener> listeners = new ArrayList<>(); 

    @PostConstruct
    private void init() {
        DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0", Locale.getDefault().toString());
        try {
            client = new DbxClient(config, accessToken);
            addRefreshListener();
            LOGGER.info("Linked account: " + client.getAccountInfo().displayName);
        } catch (DbxException e) {
            throw new StorageException("Error during connection to Dropbox", e);
        }
    }
   
    public void save(String fileName, long length, InputStream inputStream) {
        try {
            client.uploadFile(fileName, DbxWriteMode.add(), length, inputStream);
        } catch (DbxException e) {
            throw new StorageException("Error during connection to Dropbox", e);
        } catch (IOException e) {
            throw new StorageException("File not found", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public ByteArrayOutputStream getFile(String path, String fileName) {
        return getFile(path + fileName);
    }
    
    public ByteArrayOutputStream getFile(String path) {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            DbxEntry.File downloadedFile = client.getFile(path, null, outputStream);
            LOGGER.debug("Metadata: " + downloadedFile.toString());
        } catch (DbxException e) {
            throw new StorageException("Error during connection to Dropbox", e);
        } catch (IOException e) {
            throw new StorageException("File not found", e);
        }
        return outputStream;
    }
    
    public void delete(String path) {
        try {
            client.delete(path);
        } catch (DbxException e) {
            throw new StorageException("Error during connection to Dropbox", e);
        }
    }
    
    public List<StorageEntry> getItems(String path) {
        List<StorageEntry> children = new ArrayList<>();
        try {
            DbxEntry.WithChildren listing = client.getMetadataWithChildren(path);
            if (listing.children != null) {
                for (DbxEntry entry : listing.children) {
                    children.add(new StorageEntry(entry.path, entry.isFolder()));
                }
            }
        } catch (DbxException e) {
            throw new StorageException("Error during connection to Dropbox", e);
        }
        return children;
    }
    
    public StorageEntry getItem(String path) {
        DbxEntry listing = null;
        try {
            listing = client.getMetadata(path);
        } catch (DbxException e) {
            throw new StorageException("Error during connection to Dropbox", e);
        }
        return new StorageEntry(listing.path, listing.isFolder());
    }
    
    public void createFolder(String path) {
        try {
            client.createFolder(path);
        } catch (DbxException e) {
            throw new StorageException("Error during connection to Dropbox", e);
        }
    }
    
    public Map<String, List<StorageEntry>> getAll() {
        Map<String, List<StorageEntry>> entityMap = new TreeMap<>();
        try {
            DbxDelta<DbxEntry> result = client.getDeltaWithPathPrefix(null, "/");
            for (DbxDelta.Entry entry : result.entries) {
                String parent = getParentPath(entry.lcPath);
                List<StorageEntry> items;
                if (entityMap.containsKey(parent)) {
                    items = entityMap.get(parent);
                } else {
                    items = new ArrayList<>();
                    entityMap.put(parent, items);
                }
                DbxEntry dbx = (DbxEntry) entry.metadata;
                items.add(new StorageEntry(dbx.path, dbx.isFolder()));
            }
        } catch (DbxException e) {
            throw new StorageException("Error during connection to Dropbox", e);
        }
        return entityMap;
    }
    
    public String getParentPath(String path) {
        String parentPath = StringUtils.substring(path, 0, StringUtils.lastIndexOf(path, "/"));
        if (StringUtils.isEmpty(parentPath)) {
            parentPath = "/";
        }
        return parentPath;
    }

    public void addUpdateListener(StorageListener listener) {
        listeners.add(listener);
    }
    
    public void removeUploadListener(StorageListener listener) {
        listeners.remove(listener);
    }
    
    private void addRefreshListener() {
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            String cursor = null;
            @Override
            public void run() {
                try {
                    DbxDelta<DbxEntry> result = client.getDeltaWithPathPrefix(cursor, "/");
                    cursor = result.cursor;
                    if (result.reset) {
                        System.out.println("Reset!");
                    }
                    for (DbxDelta.Entry entry : result.entries) {
                        if (entry.metadata == null) {
                            System.out.println("Deleted: " + entry.lcPath);
                            String parent = getParentPath(entry.lcPath);
                            fireRemoveItem(new StorageEntry(entry.lcPath, false), getItem(parent));
                        } else {
                            System.out.println("Added or modified: " + entry.lcPath);
                            String parent = getParentPath(entry.lcPath);
                            fireAddItem(new StorageEntry(entry.lcPath, ((DbxEntry) entry.metadata).isFolder()), getItem(parent));
                        }
                    }
                } catch (DbxException e) {
                    throw new StorageException("Error during connection to Dropbox", e);
                }
            }
        }, 2000, 2000);
    }
    
    private void fireAddItem(StorageEntry item, StorageEntry parent) {
        for (StorageListener listener : listeners) {
            listener.addItemEvent(item, parent);
        }
    }
    
    private void fireRemoveItem(StorageEntry item, StorageEntry parent) {
        for (StorageListener listener : listeners) {
            listener.removeItemEvent(item, parent);
        }
    }
}

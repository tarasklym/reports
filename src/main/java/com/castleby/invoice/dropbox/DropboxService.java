package com.castleby.invoice.dropbox;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.castleby.invoice.exception.StorageException;
import com.castleby.invoice.upload.DropboxReceiver;
import com.dropbox.core.DbxClient;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(DropboxReceiver.class);

    @Value("${dropbox.accessToken}")
    private String accessToken;
    
    private DbxClient client;

    @PostConstruct
    private void init() {
        DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0", Locale.getDefault().toString());
        try {
            client = new DbxClient(config, accessToken);
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

    public FileOutputStream get(String path, String fileName) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(fileName);
            DbxEntry.File downloadedFile = client.getFile(path + fileName, null,
                outputStream);
            System.out.println("Metadata: " + downloadedFile.toString());
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
    
    public List<DbxEntry> getItems(String path) {
        List<DbxEntry> children = null;
        try {
            DbxEntry.WithChildren listing = client.getMetadataWithChildren(path);
            System.out.println("Files in the root path:");
            children = listing.children;
        } catch (DbxException e) {
            throw new StorageException("Error during connection to Dropbox", e);
        }
        return children;
    }
    
    public DbxEntry.WithChildren getItem(String path) {
        DbxEntry.WithChildren listing = null;
        try {
            listing = client.getMetadataWithChildren(path);
        } catch (DbxException e) {
            throw new StorageException("Error during connection to Dropbox", e);
        }
        return listing;
    }
    
    public void createFolder(String path) {
        try {
            client.createFolder(path);
        } catch (DbxException e) {
            throw new StorageException("Error during connection to Dropbox", e);
        }
    }
}

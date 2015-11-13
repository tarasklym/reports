package com.castleby.invoice.dropbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.castleby.invoice.TestBaseClass;
import com.castleby.invoice.storage.StorageEntry;

public class TestDropbox extends TestBaseClass {

    private static final String FILE_PATH = "/README.md";
    
    @Autowired
    private DropboxService service;
    
    @Test(groups = { "create" })
    public void testUpload() throws FileNotFoundException {
        File inputFile = new File("README.md");
        Assert.assertTrue(inputFile.exists());
        FileInputStream inputStream = new FileInputStream(inputFile);
        service.save(FILE_PATH, inputFile.length(), inputStream);
    }
    
    @Test(groups = { "read" }, dependsOnGroups = { "create" })
    public void testGetList() {
        Assert.assertFalse(service.getItems("/").isEmpty());
    }
    
    @Test(groups = { "read" }, dependsOnGroups = { "create" })
    public void testGet() {
        Assert.assertNotNull(service.getFile("/", "README.md"));
    }
    
    @Test(groups = { "delete" }, dependsOnGroups = { "read" })
    public void testDelete() throws FileNotFoundException {
        service.delete(FILE_PATH);
    }

    public void testFolder() {
        service.createFolder("/testFolder");
        Assert.assertNull(service.getItem("/testFolderNotFound"));
        Assert.assertNotNull(service.getItem("/testFolder"));
        service.delete("/testFolder");
    }
    
    @Test
    public void testGetAll() {
        Map<String, List<StorageEntry>> all = service.getAll();
        for (String path : all.keySet()) {
           System.out.println("----parent: " + path);
           for (StorageEntry storageEntry : all.get(path)) {
               System.out.println("children: " + storageEntry.getPath());    
           }
        }
    }
}

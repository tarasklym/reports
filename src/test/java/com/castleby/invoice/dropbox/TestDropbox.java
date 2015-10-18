package com.castleby.invoice.dropbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.castleby.invoice.TestBaseClass;

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
        Assert.assertNotNull(service.get("/", "README.md"));
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
}

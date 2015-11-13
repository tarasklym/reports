package com.castleby.invoice.upload;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

/**
 * 
 * @author taras.klym
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractStreamReceiver implements Receiver, SucceededListener {

    private static final String PATH_SEPARATOR = "/";
    private ByteArrayOutputStream outStream;
    private String folderPath;
    private String filename;

    /**
     * return an OutputStream that simply counts line-ends
     */
    @Override
    public OutputStream receiveUpload(String filename, String MIMEType) {
        this.filename = filename;
        outStream = new ByteArrayOutputStream();
        return outStream; 
    }

    /**
     * @return the outStream
     */
    public ByteArrayOutputStream getOutStream() {
        return outStream;
    }

    @Override
    public void uploadSucceeded(SucceededEvent event) {
        save(PATH_SEPARATOR + folderPath + PATH_SEPARATOR + filename, -1);
    }
    
    public abstract void save(String fileName, long length);

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}

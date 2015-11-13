package com.castleby.invoice.upload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.castleby.invoice.dropbox.DropboxService;

/**
 * 
 * @author taras.klym
 *
 */
@Component
@SuppressWarnings("serial")
public class DropboxReceiver extends AbstractStreamReceiver {
    
    @Autowired
    private DropboxService service;

    @Override
    public void save(String fileName, long length) {
        ByteArrayOutputStream outStream = null;
        try {
            outStream = getOutStream();
            InputStream inputStream = new ByteArrayInputStream(outStream.toByteArray());
            service.save(fileName, length, inputStream);
        } finally {
            IOUtils.closeQuietly(outStream);
        }
    }


}

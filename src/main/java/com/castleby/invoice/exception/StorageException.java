package com.castleby.invoice.exception;

/**
 * 
 * @author taras.klym
 *
 */
@SuppressWarnings("serial")
public class StorageException extends RuntimeException {
    
    public StorageException(String message) {
        super(message);
    }
    
    public StorageException(String message, Throwable ex) {
        super(message, ex);
    }
}

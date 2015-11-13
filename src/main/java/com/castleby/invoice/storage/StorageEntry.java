package com.castleby.invoice.storage;

import java.time.Month;

import org.apache.commons.lang3.StringUtils;

public class StorageEntry {
    
    public enum Type {
        YEAR,
        MONTH,
        DOCUMENT
    }
    
    private String path;
    private String name;
    private boolean isFolder = false;
      
    public StorageEntry(String path, boolean isFolder) {
        this.path = path;
        this.isFolder = isFolder;
        this.name = StringUtils.substringAfterLast(path, "/");
        if (Type.MONTH == getType()) {
            name = Month.of(Integer.valueOf(name)).name();
        }
    }
    
    public String getName() {
        return this.name;
    }
        
    public String getPath() {
        return this.path;
    }
    
    public Type getType() {
        Type type = Type.DOCUMENT;
        String[] elements = StringUtils.split(getPath(), "/");
        if (isFolder && elements.length == 1) {
            type = Type.YEAR;
        } else if (isFolder && elements.length == 2) {
            type = Type.MONTH;
        }
        return type;
    }
   
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StorageEntry other = (StorageEntry) obj;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        return true;
    }


}

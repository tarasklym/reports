package com.castleby.invoice.component.tree.listener;

import com.castleby.invoice.storage.StorageEntry;

public interface StorageListener {
    void addItemEvent(StorageEntry item, StorageEntry parent);
    void removeItemEvent(StorageEntry item, StorageEntry parent);
}

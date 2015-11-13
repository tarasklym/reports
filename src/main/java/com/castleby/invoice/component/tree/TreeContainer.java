package com.castleby.invoice.component.tree;

import java.util.List;
import java.util.Map;

import com.castleby.invoice.dropbox.DropboxService;
import com.castleby.invoice.storage.StorageEntry;
import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;

@SuppressWarnings("serial")
public class TreeContainer extends HierarchicalContainer {

    public static final String NAME_PROPERTY = "name";
    private Map<String, List<StorageEntry>> all;
    private DropboxService dropboxService;
    
    public TreeContainer(DropboxService dropboxService) {
        this.dropboxService = dropboxService;
        addContainerProperty(NAME_PROPERTY, String.class, "");
        all = dropboxService.getAll();
        addItems("/", null);
    }

    public void addItems(String path, StorageEntry parent) {
        List<StorageEntry> items = all.get(path);
        if (items == null || items.isEmpty()) {
            setChildrenAllowed(parent, false);
        } else {
            for (StorageEntry entyty : items) {
                addItem(entyty);
                if (parent != null) {
                    setParent(entyty, parent);
                }
                addItems(entyty.getPath(), entyty);
            }
        }
    }
    
    public Item addItem(StorageEntry entyty) {
        Item item = super.addItem(entyty);
        if (item != null) {
            item.getItemProperty(NAME_PROPERTY).setValue(entyty.getName());
        }
        return item;
    }
    
    public void refresh() {
        all = dropboxService.getAll();
        addItems("/", null);
    }
                
}

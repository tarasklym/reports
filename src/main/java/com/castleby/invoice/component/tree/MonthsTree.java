package com.castleby.invoice.component.tree;

import com.castleby.invoice.component.tree.listener.StorageListener;
import com.castleby.invoice.dropbox.DropboxService;
import com.castleby.invoice.storage.StorageEntry;
import com.castleby.invoice.view.DocumentView;
import com.castleby.invoice.view.InvoiceView;
import com.castleby.invoice.view.MainView;
import com.vaadin.data.Property;
import com.vaadin.ui.Tree;

@SuppressWarnings("serial")
public class MonthsTree extends Tree {
    
    private TreeContainer container;
    private StorageListener storageListener;
    private DropboxService dropboxService;
    
    public MonthsTree(DropboxService dropboxService) {
        this.dropboxService = dropboxService;
        setImmediate(true);
        container = new TreeContainer(dropboxService);
        setContainerDataSource(container);
        setItemCaptionMode(ItemCaptionMode.PROPERTY);
        setItemCaptionPropertyId(TreeContainer.NAME_PROPERTY);
        addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                StorageEntry entry = (StorageEntry) event.getProperty().getValue();
                if (entry == null) {
                    nothingSelected();
                } else {
                    switch (entry.getType()) {
                    case YEAR:
                        selectedYear(entry);
                        break;
                    case MONTH:
                        selectedMonth(entry);
                        break;
                    default:
                        selectedDocument(entry);
                        break;
                    }
                }
            }
        });
        inidUploadListener();
    }

    private void inidUploadListener() {
        storageListener = new StorageListener() {
            @Override
            public void addItemEvent(StorageEntry item, StorageEntry parent) {
                getUI().access(new Runnable() {
                    @Override
                    public void run() {
                        container.addItem(item);
                        container.setChildrenAllowed(item, false);
                        container.setChildrenAllowed(parent, true);
                        container.setParent(item, parent);
                    }
                });
            }
            @Override
            public void removeItemEvent(StorageEntry item, StorageEntry parent) {
                getUI().access(new Runnable() {
                    @Override
                    public void run() {
                        removeItem(item);
                        setChildrenAllowed(parent, container.hasChildren(parent));
                    }
                });
            }
        };
    }
    
    private void nothingSelected() {
        getUI().getNavigator().navigateTo(MainView.NAME);
    }
    
    private void selectedYear(StorageEntry entry) {
        getUI().getNavigator().navigateTo(MainView.NAME);
    }
    
    private void selectedMonth(StorageEntry entry) {
        getUI().getNavigator().navigateTo(InvoiceView.NAME + entry.getPath());
    }
    
    private void selectedDocument(StorageEntry entry) {
        getUI().getNavigator().navigateTo(DocumentView.NAME + entry.getPath());
    }
    
    @Override
    public void attach() {
        super.attach();
        dropboxService.addUpdateListener(storageListener);
    }
    
    @Override
    public void detach() {
        dropboxService.removeUploadListener(storageListener);
        super.detach();
    }
}

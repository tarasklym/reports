package com.castleby.invoice.view;

import java.util.ArrayList;
import java.util.List;

import com.castleby.invoice.entity.InvoiceItem;
import com.castleby.invoice.entity.ItemType;
import com.castleby.invoice.entity.Tax.Type;
import com.castleby.invoice.entity.repo.TaxRepository;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class InvoiceItemWindow extends Window {

    private List<SaveListener> saveListeners = new ArrayList<>();
    
    private TextField name = new TextField("Name");
    private TextField amount = new TextField("Netto");
    private ComboBox type = new ComboBox("Type");
    private Button save = new Button("save");
    private Button cancel = new Button("cancel");
    private InvoiceItem item;
    
    public InvoiceItemWindow(TaxRepository taxRepository) {
        setModal(true);
        setClosable(false);
        setResizable(false);
        save.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                item.setDescription(name.getValue());
                Double nettoValue = Double.valueOf(amount.getValue());
                item.setNetto(nettoValue);
                ItemType selectedType = (ItemType) type.getValue();
                item.setType(selectedType);
                double extraTaxRate = selectedType == ItemType.SALARY ? 1 : taxRepository.findByType(Type.INCOME).getValue();
                item.setExtraTaxRate(extraTaxRate);
                item.setAmount(nettoValue * extraTaxRate);
                for (SaveListener listener : saveListeners) {
                    listener.save(item);
                }
                close();
            }
        });
        cancel = new Button("cancel", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                close();
            }
        });
        HorizontalLayout buttonLayout = new HorizontalLayout(save, cancel);
        buttonLayout.setSpacing(true);
        VerticalLayout mainLayout = new VerticalLayout(name, amount, type, buttonLayout);
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);
        setContent(mainLayout);
    }
    
    public void addSaveListener(SaveListener listener) {
        saveListeners.add(listener);
    }

    public void setItem(InvoiceItem invoiceItem) {
        this.item = invoiceItem;
        name.setValue(invoiceItem.getDescription());
        amount.setValue(String.valueOf(invoiceItem.getNetto()));
        type.addItems(ItemType.OTHER, ItemType.SALARY);
        type.setValue(invoiceItem.getType());
    }
    
    public interface SaveListener {
        void save(InvoiceItem item);
    }

}

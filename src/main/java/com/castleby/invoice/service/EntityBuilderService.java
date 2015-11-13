package com.castleby.invoice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.castleby.invoice.entity.Invoice;
import com.castleby.invoice.entity.InvoiceItem;
import com.castleby.invoice.entity.ItemType;
import com.castleby.invoice.entity.repo.InvoiceItemRepository;
import com.castleby.invoice.entity.repo.InvoiceRepository;

@Service
public class EntityBuilderService {
    
    @Autowired
    private InvoiceRepository invoiceRepository;
    
    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    public Invoice buildInvoice() {
        Invoice invoice = new Invoice();
        invoice = invoiceRepository.saveAndFlush(invoice);
        for (InvoiceItem item : invoiceItemRepository.findByType(ItemType.DEFAULT)) {
            InvoiceItem invoiceItem = buildInvoiceItem(ItemType.OTHER, item.getDescription(), item.getNetto(), item.getExtraTaxRate());
            invoiceItem.setInvoice(invoice);
            invoiceItemRepository.saveAndFlush(invoiceItem);
        }
        for (InvoiceItem item : invoiceItemRepository.findByType(ItemType.DEFAULT_SALARY)) {
            InvoiceItem invoiceItem = buildInvoiceItem(ItemType.SALARY, item.getDescription(), item.getAmount(), item.getExtraTaxRate());
            invoiceItem.setInvoice(invoice);
            invoiceItemRepository.saveAndFlush(invoiceItem);
        }
        return invoice;
    }
    
    public InvoiceItem buildInvoiceItem(ItemType type, String description, double netto, double extraTaxRate) {
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setType(type);
        invoiceItem.setDescription(description);
        invoiceItem.setNetto(netto);
        invoiceItem.setExtraTaxRate(extraTaxRate);
        invoiceItem.setAmount(netto * extraTaxRate);
        return invoiceItem;
    }
}

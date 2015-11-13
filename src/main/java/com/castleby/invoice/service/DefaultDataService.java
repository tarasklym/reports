package com.castleby.invoice.service;

import java.time.YearMonth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.castleby.invoice.dropbox.DropboxService;
import com.castleby.invoice.entity.InvoiceItem;
import com.castleby.invoice.entity.ItemType;
import com.castleby.invoice.entity.Tax;
import com.castleby.invoice.entity.Tax.Type;
import com.castleby.invoice.entity.repo.InvoiceItemRepository;
import com.castleby.invoice.entity.repo.TaxRepository;

@Service
public class DefaultDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDataService.class);
    @Autowired
    private InvoiceItemRepository invoiceItemRepository;
    @Autowired
    private TaxRepository taxRepository;
    @Autowired
    private EntityBuilderService entityBuilderService;
    @Autowired
    private DropboxService dropboxService;
    
    public void init() {
        if (taxRepository.findAll().size() != 2) {
            initDefaultTaxes();
        }
        if (invoiceItemRepository.findByType(ItemType.DEFAULT_SALARY).isEmpty()) {
            initDefaultInvoiceItem();
        }
        if (dropboxService.getItem("/2015") == null) {
            initStorageStructure();
        }
    }
    
    private void initDefaultTaxes() {
        LOGGER.info("Add default taxes");
        Tax fixedTax = new Tax();
        fixedTax.setType(Type.FIXED);
        fixedTax.setDescription("Pension fond");
        fixedTax.setValue(365);
        taxRepository.saveAndFlush(fixedTax);
        
        Tax incomeTax = new Tax();
        incomeTax.setType(Type.INCOME);
        incomeTax.setDescription("Income tax");
        incomeTax.setValue(1.05);
        taxRepository.saveAndFlush(incomeTax);
    }

    private void initDefaultInvoiceItem() {
        LOGGER.info("Add default invoice items");
        
        Tax incomeTax = taxRepository.findByType(Type.INCOME);
        
        InvoiceItem salaryItem = entityBuilderService.buildInvoiceItem(ItemType.DEFAULT_SALARY, "salary", 1000, 1);
        invoiceItemRepository.saveAndFlush(salaryItem);
        
        InvoiceItem sportItem = entityBuilderService.buildInvoiceItem(ItemType.DEFAULT, "sport", 15, incomeTax.getValue());
        invoiceItemRepository.saveAndFlush(sportItem);
        
        InvoiceItem accountantItem = entityBuilderService.buildInvoiceItem(ItemType.DEFAULT, "accountant", 20, incomeTax.getValue());
        invoiceItemRepository.saveAndFlush(accountantItem);
    }
    
    private void initStorageStructure() {
        LOGGER.info("create default structure");
        String separator = "/";
        YearMonth startDate = YearMonth.of(2014, 1);
        YearMonth endDate = YearMonth.now().plusMonths(1);
        while (endDate.isAfter(startDate)) {
            dropboxService.createFolder(separator + startDate.getYear() + separator + startDate.getMonthValue());
            startDate = startDate.plusMonths(1);            
        }
    }
    
}

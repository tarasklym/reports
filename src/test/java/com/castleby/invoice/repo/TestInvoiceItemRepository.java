package com.castleby.invoice.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.castleby.invoice.TestBaseClass;
import com.castleby.invoice.entity.InvoiceItem;
import com.castleby.invoice.entity.ItemType;
import com.castleby.invoice.entity.repo.InvoiceItemRepository;

@Test
public class TestInvoiceItemRepository extends TestBaseClass {
    
    @Autowired
    private InvoiceItemRepository invoiceItemRepository;
    
    @BeforeMethod
    public void before() {
        for (InvoiceItem invoice : invoiceItemRepository.findAll()) {
            invoiceItemRepository.delete(invoice);
        }
    }
    
    @Test
    public void testFindItemByTypr() {
        InvoiceItem salaryItem = new InvoiceItem();
        salaryItem.setDescription("salary");
        salaryItem.setType(ItemType.DEFAULT);
        salaryItem.setAmount(1000);
        salaryItem = invoiceItemRepository.saveAndFlush(salaryItem);
        
        List<InvoiceItem> items = invoiceItemRepository.findByType(ItemType.DEFAULT);
        Assert.assertFalse(items.isEmpty());
        boolean isExest = false;
        for (InvoiceItem item : items) {
            if (item.equals(salaryItem)) {
                isExest = true;
                break;
            }
        }
        invoiceItemRepository.delete(salaryItem);
        Assert.assertTrue(isExest);
    }

}

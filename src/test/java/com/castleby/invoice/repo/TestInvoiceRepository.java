package com.castleby.invoice.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.castleby.invoice.TestBaseClass;
import com.castleby.invoice.entity.Invoice;

@Test
public class TestInvoiceRepository extends TestBaseClass {
    
    @Autowired
    private InvoiceRepository repository;
    
    @Test
    public void testCreateInvoice() {
        Invoice invoice = new Invoice();
        int size = repository.findAll().size();
        invoice = repository.saveAndFlush(invoice);
        Assert.assertEquals(size + 1, repository.findAll().size());
    }
    
    @Test
    public void testDeleteInvoice() {
        Invoice invoice = new Invoice();
        int size = repository.findAll().size();
        invoice = repository.saveAndFlush(invoice);
        repository.delete(invoice.getId());
        Assert.assertEquals(size, repository.findAll().size());
    }
}

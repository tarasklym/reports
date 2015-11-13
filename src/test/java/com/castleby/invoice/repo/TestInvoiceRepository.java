package com.castleby.invoice.repo;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.castleby.invoice.TestBaseClass;
import com.castleby.invoice.entity.Invoice;
import com.castleby.invoice.entity.repo.InvoiceRepository;

@Test
public class TestInvoiceRepository extends TestBaseClass {
    
    @Autowired
    private InvoiceRepository repository;
    
    @BeforeMethod
    public void before() {
        for (Invoice invoice : repository.findAll()) {
            repository.delete(invoice);
        }
    }
    
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
    
    @Test
    public void testFindByDateInvoice() {
        Date currentDate = new Date();
        Invoice invoice = new Invoice();
        invoice.setDate(currentDate);
        invoice = repository.saveAndFlush(invoice);
        
        Date startDate = new Date(currentDate.getYear(), currentDate.getMonth(), 1);
        LocalDate initial = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1);
        Date endDate = new Date(currentDate.getYear(), currentDate.getMonth(), initial.lengthOfMonth());
        Invoice findByDateBetween = repository.findByDateBetween(startDate, endDate);
        Assert.assertEquals(invoice, findByDateBetween);
    }
}

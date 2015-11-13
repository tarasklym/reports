package com.castleby.invoice.entity.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.castleby.invoice.entity.Invoice;
import com.castleby.invoice.entity.InvoiceItem;
import com.castleby.invoice.entity.ItemType;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {

    List<InvoiceItem> findByInvoice(Invoice invoice);
    
    List<InvoiceItem> findByType(ItemType type);
}

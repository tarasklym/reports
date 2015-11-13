package com.castleby.invoice.entity.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.castleby.invoice.entity.Tax;

@Repository
public interface TaxRepository extends JpaRepository<Tax, Long> {
    
    Tax findByType(Tax.Type type);
}

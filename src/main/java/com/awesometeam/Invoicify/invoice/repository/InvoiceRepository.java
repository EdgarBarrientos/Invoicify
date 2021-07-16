package com.awesometeam.Invoicify.invoice.repository;

import com.awesometeam.Invoicify.invoice.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Transactional
    @Query(value = "SELECT i FROM Invoice i where i.company.Id = :id and i.Status = :status")
    Page<Invoice> findByCompanyIdAndStatus(Long id, String status, Pageable pageable);

}

package com.awesometeam.Invoicify.invoice.repository;

import com.awesometeam.Invoicify.invoice.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long>
{
    @Transactional
    @Modifying
    @Query("update Invoice inv set inv.cost = :total where inv.invoiceId = :invoiceId")
    void updateCost(@Param("invoiceId")long invoiceId, @Param("total") double total);

}

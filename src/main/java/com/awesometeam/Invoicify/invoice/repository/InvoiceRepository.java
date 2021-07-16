package com.awesometeam.Invoicify.invoice.repository;

import com.awesometeam.Invoicify.invoice.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InvoiceRepository extends JpaRepository<Invoice, Long>
{
    @Query("update Invoice inv set inv.cost = :cost where inv.invoiceId = :invoiceId")
    Invoice updateCost(long invoiceId, double cost);
}

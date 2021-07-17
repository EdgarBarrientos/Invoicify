package com.awesometeam.Invoicify.invoice.repository;

import com.awesometeam.Invoicify.invoice.model.InvoiceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface InvoiceDetailsRepository extends JpaRepository<InvoiceDetails,Long>
{
    @Transactional
    @Query("select coalesce(sum(totalPrice),0.0) from InvoiceDetails i where i.invoiceId=:invoiceId")
    double getCost(@Param("invoiceId") long invoiceId);
}

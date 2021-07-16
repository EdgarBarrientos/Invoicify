package com.awesometeam.Invoicify.invoice.repository;

import com.awesometeam.Invoicify.invoice.model.InvoiceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InvoiceDetailsRepository extends JpaRepository<InvoiceDetails,Long>
{
    @Query("select sum(totalPrice) from invoiceDetails i where i.invoiceId=:invoiceId")
    double getCost(long invoiceId);
}

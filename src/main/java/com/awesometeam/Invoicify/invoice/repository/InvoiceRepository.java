package com.awesometeam.Invoicify.invoice.repository;

import com.awesometeam.Invoicify.invoice.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

public interface InvoiceRepository extends JpaRepository<Invoice, Long>
{
    @Transactional
    @Modifying
    @Query("update Invoice inv set inv.cost = :total where inv.invoiceId = :invoiceId")
    void updateCost(@Param("invoiceId")long invoiceId, @Param("total") double total);

    @Transactional
    @Modifying
    @Query("update Invoice inv set inv.invoiceDate = :invoiceDate where inv.invoiceId = :invoiceId")
    void updateInvoiceDate(@Param("invoiceDate") LocalDate invoiceDate,@Param("invoiceId") long invoiceId);

    @Transactional
    @Modifying
    @Query("update Invoice inv set inv.Status = :status where inv.invoiceId = :invoiceId")
  void updateInvoiceStatus(@Param("status") String status,@Param("invoiceId") long invoiceId);

    @Transactional
    @Modifying
    @Query("update Invoice inv set inv.Status = :status , inv.invoiceDate = :invoiceDate where inv.invoiceId = :invoiceId")
  void updateInvoiceDateAndStatus(@Param("status")String status,@Param("invoiceDate")  LocalDate invoiceDate, @Param("invoiceId")long invoiceId);
}

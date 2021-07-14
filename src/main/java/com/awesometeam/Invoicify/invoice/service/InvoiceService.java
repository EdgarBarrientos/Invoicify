package com.awesometeam.Invoicify.invoice.service;

import com.awesometeam.Invoicify.invoice.model.Invoice;
import com.awesometeam.Invoicify.invoice.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {
    @Autowired
    InvoiceRepository invoiceRepository;

    public Invoice createNewInvoices(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }
}

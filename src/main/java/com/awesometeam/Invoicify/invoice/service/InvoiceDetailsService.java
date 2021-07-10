package com.awesometeam.Invoicify.invoice.service;

import com.awesometeam.Invoicify.invoice.model.InvoiceDetails;
import com.awesometeam.Invoicify.invoice.repository.InvoiceDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceDetailsService {
    @Autowired
    InvoiceDetailsRepository invoiceDetailsRepository;

    public InvoiceDetails addNewLineItem(InvoiceDetails invoiceDetails)
    {
        return invoiceDetailsRepository.save(invoiceDetails);
    }
}

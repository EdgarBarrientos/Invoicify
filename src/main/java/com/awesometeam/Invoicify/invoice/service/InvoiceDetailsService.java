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
        if (invoiceDetails.getLineItem().getFeeType() == 'F'){
             invoiceDetails.setTotalPrice(invoiceDetails.getLineItem().getAmount());
        }
        else{
             invoiceDetails.setTotalPrice(invoiceDetails.getLineItem().getFee() * invoiceDetails.getLineItem().getQuantity());
        }
        return invoiceDetailsRepository.save(invoiceDetails);
    }
}

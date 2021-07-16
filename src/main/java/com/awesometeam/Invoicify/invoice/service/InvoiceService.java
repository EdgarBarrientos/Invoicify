package com.awesometeam.Invoicify.invoice.service;

import com.awesometeam.Invoicify.invoice.model.Invoice;
import com.awesometeam.Invoicify.invoice.model.InvoiceDetails;
import com.awesometeam.Invoicify.invoice.repository.InvoiceDetailsRepository;
import com.awesometeam.Invoicify.invoice.repository.InvoiceRepository;
import com.awesometeam.Invoicify.invoice.repository.ItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
//import com.awesometeam.Invoicify.invoice.service.InvoiceDetailsService;

@Service
public class InvoiceService {
    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    InvoiceDetailsRepository invoiceDetailsRepository;

    @Autowired
    ItemsRepository itemsRepository;

    @Autowired
    InvoiceDetailsService invoiceDetailsService;

    public Invoice createNewInvoices(Invoice invoice)
    {
               return invoiceRepository.save(invoice);
    }

    public InvoiceDetails addNewLineItem(InvoiceDetails invoiceDetails)
    {
        if (invoiceDetails.getLineItem().getFeeType() == 'F'){
            invoiceDetails.setTotalPrice(invoiceDetails.getLineItem().getAmount());
        }
        else{
            invoiceDetails.setTotalPrice(invoiceDetails.getLineItem().getFee() * invoiceDetails.getLineItem().getQuantity());
        }
        System.out.println("invoice ID " + invoiceDetails.getInvoiceId());
        double total = invoiceDetailsRepository.getCost(invoiceDetails.getInvoiceId()) + invoiceDetails.getTotalPrice() ;
        System.out.println("total " + total);
        invoiceRepository.updateCost(invoiceDetails.getInvoiceId(),total);
        return invoiceDetailsRepository.save(invoiceDetails);
    }
    public Invoice findByInvoiceId(long invoiceId) {
        Optional<Invoice> invoice = invoiceRepository.findById(invoiceId);
        return invoice.get();
    }
}

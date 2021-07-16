package com.awesometeam.Invoicify.invoice.service;

import com.awesometeam.Invoicify.invoice.model.Invoice;
import com.awesometeam.Invoicify.invoice.model.InvoiceDetails;
import com.awesometeam.Invoicify.invoice.repository.InvoiceDetailsRepository;
import com.awesometeam.Invoicify.invoice.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InvoiceDetailsService {
    @Autowired
    InvoiceDetailsRepository invoiceDetailsRepository;

    @Autowired
    InvoiceRepository invoiceRepository;
   // InvoiceService invoiceService=new InvoiceService();

//    public InvoiceDetails addNewLineItem(InvoiceDetails invoiceDetails)
//    {
//        if (invoiceDetails.getLineItem().getFeeType() == 'F'){
//             invoiceDetails.setTotalPrice(invoiceDetails.getLineItem().getAmount());
//        }
//        else{
//             invoiceDetails.setTotalPrice(invoiceDetails.getLineItem().getFee() * invoiceDetails.getLineItem().getQuantity());
//        }
//        System.out.println("invoice ID " + invoiceDetails.getInvoiceId());
//        double total = invoiceDetailsRepository.getCost(invoiceDetails.getInvoiceId()) + invoiceDetails.getTotalPrice() ;
//        System.out.println("total " + total);
//        invoiceRepository.updateCost(invoiceDetails.getInvoiceId(),total);
//        return invoiceDetailsRepository.save(invoiceDetails);
//    }
}

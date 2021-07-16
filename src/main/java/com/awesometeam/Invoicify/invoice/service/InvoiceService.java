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
        //Invoice invoice1 = new Invoice(invoice.getCompany(), new Date(2021,07,12),invoice.getStatus()
          //     ,new Date(2021,07,12),0.0,null);
//        Invoice invoice=new Invoice(company, new Date(2021,07,12)
//                ,"Unpaid",new Date (2021,07,12) ,0.0, null );
        //System.out.println(invoice.getCompany() + invoice.getInvoiceDate().toString() +invoice.getStatus()
          //      + invoice.getModifiedDate());
        //System.out.println(invoice1.getCompany() + invoice1.getInvoiceDate().toString() +invoice1.getStatus()
         //       + invoice1.getModifiedDate());
        //System.out.println(invoice1.toString());
        Invoice invoice2 = invoiceRepository.save(invoice);
        return invoice2;
//        if (invoice.getInvoiceDetailsList().size() != 0)
//        {
//            //List<InvoiceDetails> invoiceDetailsList1 = new ArrayList<>();
//            System.out.println(invoice.getInvoiceDetailsList().size());
//            double invoiceTotal = 0;
//            for (InvoiceDetails invoiceLineItems:invoice.getInvoiceDetailsList())
//            {
////                invoiceLineItems.setInvoiceId(invoice2.getInvoiceId());
////                invoiceDetailsService.addNewLineItem(invoiceLineItems);
//                invoiceTotal += invoiceLineItems.getTotalPrice();
//            }
//            invoice.setCost(invoiceTotal);
//        }
//        invoiceRepository.updateCost(invoice.getInvoiceId(),invoice.getCost());
//        return invoiceRepository.findById(invoice.getInvoiceId());

    }

    public Invoice findByInvoiceId(long invoiceId) {
        Optional<Invoice> invoice = invoiceRepository.findById(invoiceId);
        return invoice.get();
    }
}

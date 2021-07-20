package com.awesometeam.Invoicify.invoice.service;

import com.awesometeam.Invoicify.invoice.model.Invoice;
import com.awesometeam.Invoicify.invoice.model.InvoiceDetails;
import com.awesometeam.Invoicify.invoice.repository.InvoiceDetailsRepository;
import com.awesometeam.Invoicify.invoice.repository.InvoiceRepository;
import com.awesometeam.Invoicify.invoice.repository.ItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {
    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    InvoiceDetailsRepository invoiceDetailsRepository;

    @Autowired
    ItemsRepository itemsRepository;

    public Invoice createNewInvoices(Invoice invoice)
    {
        Invoice invoice1 = invoiceRepository.save(invoice);
        if(invoice.getInvoiceDetailsList() != null && invoice.getInvoiceDetailsList().size() !=0) {
            lineItemCheckAndInsert(invoice.getInvoiceDetailsList(), invoice1.getInvoiceId());
        }
               return invoice1;
    }

    private void lineItemCheckAndInsert(List<InvoiceDetails> invoiceDetailsList, long invoiceId) {
            for (InvoiceDetails invoiceDetails: invoiceDetailsList) {
                invoiceDetails.setInvoiceId(invoiceId);
                addNewLineItem(invoiceDetails);
            }
    }

    public InvoiceDetails addNewLineItem(InvoiceDetails invoiceDetails)
    {
        if (invoiceDetails != null && invoiceDetails.getLineItem() != null) {
            itemsRepository.save(invoiceDetails.getLineItem());
        }
        invoiceDetails.setTotalPrice(calculateTotalPrice(invoiceDetails));
        updateInvoiceCost(invoiceDetails, invoiceDetails.getInvoiceId());
        return invoiceDetailsRepository.save(invoiceDetails);
    }

    private void updateInvoiceCost(InvoiceDetails invoiceDetails, long invoiceId) {
        double total = 0.0;
        double priorTotal = invoiceDetailsRepository.getCost(invoiceId);

        if (priorTotal != 0.0 )
        {
            total += priorTotal;
        }
        total += invoiceDetails.getTotalPrice();
        invoiceRepository.updateCost(invoiceDetails.getInvoiceId(),total);
    }

    private double calculateTotalPrice(InvoiceDetails invoiceDetails) {
        if (invoiceDetails.getLineItem().getFeeType() == 'F'){
            return (invoiceDetails.getLineItem().getAmount());
        }
        else{
            return (invoiceDetails.getLineItem().getFee() * invoiceDetails.getLineItem().getQuantity());
        }
    }


    public Invoice findByInvoiceId(long invoiceId) {
        Optional<Invoice> invoice = invoiceRepository.findById(invoiceId);
        return invoice.get();
    }

    public Optional <Invoice> modifyInvoice(Invoice invoice,long invoiceId)
    {
        Optional<Invoice> checkInv=invoiceRepository.findById(invoiceId);
        if (checkInv == null || checkInv.isEmpty() ){
            return Optional.ofNullable(null);
        };
        if (checkInv.get().getStatus() == "Paid" ){
            return Optional.ofNullable(null);
        };

            if (invoice.getInvoiceDate() == null && invoice.getStatus() == null && invoice.getCompany() == null) {
            } else if (invoice.getInvoiceDate() != null && invoice.getStatus() == null) {
                invoiceRepository.updateInvoiceDate(invoice.getInvoiceDate(), LocalDate.now(), invoiceId);
            } else if (invoice.getInvoiceDate() == null && invoice.getStatus() != null) {
                invoiceRepository.updateInvoiceStatus(invoice.getStatus(), LocalDate.now(), invoiceId);
            } else if (invoice.getInvoiceDate() != null && invoice.getStatus() != null) {
                invoiceRepository.updateInvoiceDateAndStatus(invoice.getStatus(), invoice.getInvoiceDate(), LocalDate.now(), invoiceId);
            } else if (invoice.getCompany() != null) {
                invoiceRepository.updateCompany(invoice.getCompany(), LocalDate.now(), invoiceId);
            }

        return invoiceRepository.findById(invoiceId);
    }
}

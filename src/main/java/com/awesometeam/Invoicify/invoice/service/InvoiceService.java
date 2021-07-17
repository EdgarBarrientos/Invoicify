package com.awesometeam.Invoicify.invoice.service;

import com.awesometeam.Invoicify.invoice.model.Invoice;
import com.awesometeam.Invoicify.invoice.model.InvoiceDetails;
import com.awesometeam.Invoicify.invoice.repository.InvoiceDetailsRepository;
import com.awesometeam.Invoicify.invoice.repository.InvoiceRepository;
import com.awesometeam.Invoicify.invoice.repository.ItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
               return invoiceRepository.save(invoice);
    }

    public InvoiceDetails addNewLineItem(InvoiceDetails invoiceDetails)
    {
        if (invoiceDetails != null && invoiceDetails.getLineItem() != null) {
            itemsRepository.save(invoiceDetails.getLineItem());
        }

        if (invoiceDetails.getLineItem().getFeeType() == 'F'){
            invoiceDetails.setTotalPrice(invoiceDetails.getLineItem().getAmount());
        }
        else{
            invoiceDetails.setTotalPrice(invoiceDetails.getLineItem().getFee() * invoiceDetails.getLineItem().getQuantity());
        }
        System.out.println("invoice ID " + invoiceDetails.getInvoiceId());
        double total = 0.0;
        double prirorTotal = invoiceDetailsRepository.getCost(invoiceDetails.getInvoiceId());
        System.out.println(prirorTotal);
        if (prirorTotal == 0.0 )
        {
             total += invoiceDetailsRepository.getCost(invoiceDetails.getInvoiceId());

        }
            total += invoiceDetails.getTotalPrice();
        invoiceRepository.updateCost(invoiceDetails.getInvoiceId(),total);
        return invoiceDetailsRepository.save(invoiceDetails);
    }
    public Invoice findByInvoiceId(long invoiceId) {
        Optional<Invoice> invoice = invoiceRepository.findById(invoiceId);
        return invoice.get();
    }

    public Page<Invoice> findByCompanyIdAndStatus(Long id, String status, int pageNo, int pageSize) {
        Pageable page  = PageRequest.of(pageNo, pageSize, Sort.by("invoiceDate").ascending());
        Page<Invoice> result = invoiceRepository.findByCompanyIdAndStatus(id, status, page);
        if(result.getContent().isEmpty()) {
            throw new RuntimeException();
        }
        return result;
    }


}

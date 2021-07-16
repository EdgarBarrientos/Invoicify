package com.awesometeam.Invoicify.invoice.service;

import com.awesometeam.Invoicify.invoice.model.Invoice;
import com.awesometeam.Invoicify.invoice.repository.InvoiceRepository;
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

    public Invoice createNewInvoices(Invoice invoice) {
        return invoiceRepository.save(invoice);
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

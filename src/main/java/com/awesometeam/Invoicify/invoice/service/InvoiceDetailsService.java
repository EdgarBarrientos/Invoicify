package com.awesometeam.Invoicify.invoice.service;

import com.awesometeam.Invoicify.company.repository.CompanyRepository;
import com.awesometeam.Invoicify.invoice.model.InvoiceDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceDetailsService {

    @Autowired
    InvoiceDetailsService repo;


    public void addNewLineItem(InvoiceDetails invoiceDetails)
    {

    }
}

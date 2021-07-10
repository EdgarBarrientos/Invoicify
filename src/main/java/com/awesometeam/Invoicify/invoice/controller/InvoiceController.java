package com.awesometeam.Invoicify.invoice.controller;

import com.awesometeam.Invoicify.invoice.model.InvoiceDetails;
import com.awesometeam.Invoicify.invoice.service.InvoiceDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class InvoiceController
{
    @Autowired
    InvoiceDetailsService service;

    @PostMapping("/addInvoiceItem")
    public ResponseEntity<InvoiceDetails> addLineItem(@RequestBody InvoiceDetails invoiceDetails)
    {
        invoiceDetails = service.addNewLineItem(invoiceDetails);
        return new ResponseEntity<InvoiceDetails>(invoiceDetails, HttpStatus.CREATED);
    }

}

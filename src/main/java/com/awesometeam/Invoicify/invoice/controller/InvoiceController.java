package com.awesometeam.Invoicify.invoice.controller;

import com.awesometeam.Invoicify.invoice.model.Invoice;
import com.awesometeam.Invoicify.invoice.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvoiceController {
    InvoiceService invoiceService;
    public InvoiceController(InvoiceService invoiceService){
        this.invoiceService=invoiceService;
    }

    @PostMapping("/createNewInvoice")
    public ResponseEntity createNewInvoice(@RequestBody Invoice invoice){
        invoiceService.createNewInvoices(invoice);
        return new ResponseEntity(invoice, HttpStatus.CREATED);
    }
}

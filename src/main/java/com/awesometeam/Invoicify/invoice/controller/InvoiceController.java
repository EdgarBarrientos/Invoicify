package com.awesometeam.Invoicify.invoice.controller;

import com.awesometeam.Invoicify.invoice.model.Invoice;
import com.awesometeam.Invoicify.invoice.model.InvoiceDetails;
import com.awesometeam.Invoicify.invoice.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

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

    @PatchMapping("/invoice/{invoiceId}")
    public ResponseEntity modifyInvoice(@PathVariable long invoiceId, @RequestBody Invoice invoice){
        Invoice result=invoiceService.modifyInvoice(invoice,invoiceId).orElse(null);
            if(result != null)
               return new ResponseEntity(invoice, HttpStatus.OK);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/addInvoiceItem")
    public ResponseEntity<InvoiceDetails> addLineItem(@RequestBody InvoiceDetails invoiceDetails)
    {

        invoiceService.addNewLineItem(invoiceDetails);

        return new ResponseEntity<InvoiceDetails>(invoiceDetails, HttpStatus.CREATED);
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity findInvoiceByInvoiceId(@PathVariable long invoiceId){
        Invoice invoice = invoiceService.findByInvoiceId(invoiceId);

        return new ResponseEntity(invoice, HttpStatus.OK);
    }
}

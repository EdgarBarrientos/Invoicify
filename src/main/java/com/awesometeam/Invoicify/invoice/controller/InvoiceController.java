package com.awesometeam.Invoicify.invoice.controller;

import com.awesometeam.Invoicify.invoice.dto.DtoInvoiceDetails;
import com.awesometeam.Invoicify.invoice.model.Invoice;
import com.awesometeam.Invoicify.invoice.model.InvoiceDetails;
import com.awesometeam.Invoicify.invoice.service.InvoiceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/invoiceByCompany/{id}")
    public ResponseEntity<Page<DtoInvoiceDetails>> findByCompanyId(@PathVariable long id, @RequestParam(defaultValue = "0") Integer pageNo,
                                                                   @RequestParam(defaultValue = "10") Integer pageSize) {
        Page page = null;
        try {
            page = invoiceService.findByCompanyIdAndStatus(id, "Unpaid", pageNo, pageSize);
        }
        catch (RuntimeException ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        List<DtoInvoiceDetails> dtoInvoiceDetails = (List<DtoInvoiceDetails>)page.getContent().stream().map(invoice -> DtoInvoiceDetails.mapInvoiceDetails((Invoice) invoice)).collect(Collectors.toList());
        return new ResponseEntity<>(new PageImpl<>(dtoInvoiceDetails, page.getPageable(), page.getTotalPages()), HttpStatus.OK);
    }

    @DeleteMapping("/deleteByInvoiceId/{invoiceId}")
    public ResponseEntity deleteByInvoiceId(@PathVariable long invoiceId){
        Invoice invoice = null;
        try {
        invoice = invoiceService.findByInvoiceId(invoiceId);
             invoiceService.deleteByInvoice(invoice);
       }catch (RuntimeException exception){
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(invoice, HttpStatus.OK);
    }


}

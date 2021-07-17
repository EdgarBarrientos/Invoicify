package com.awesometeam.Invoicify.invoice.dto;

import com.awesometeam.Invoicify.invoice.model.Invoice;
import com.awesometeam.Invoicify.invoice.model.InvoiceDetails;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DtoInvoiceDetails {

    Long Id;
    LocalDate createDate;
    double total;
    String paidStatus;

    List<InvoiceDetails> itemsDetails;

    public static DtoInvoiceDetails mapInvoiceDetails(Invoice invoice) {
        DtoInvoiceDetails result = new DtoInvoiceDetails();
        result.Id = invoice.getInvoiceId();
        result.createDate = invoice.getInvoiceDate();
        result.total = invoice.getCost();
        result.paidStatus = invoice.getStatus();
        result.itemsDetails = invoice.getInvoiceDetailsList();

        return result;
    };

}

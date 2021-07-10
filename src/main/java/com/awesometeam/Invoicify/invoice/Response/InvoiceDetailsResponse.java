package com.awesometeam.Invoicify.invoice.Response;

import com.awesometeam.Invoicify.invoice.model.InvoiceDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailsResponse
{
    private int status;
    private String message;
    private InvoiceDetails invoiceDetails;
}

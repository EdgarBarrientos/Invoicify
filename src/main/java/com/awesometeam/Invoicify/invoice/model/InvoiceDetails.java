package com.awesometeam.Invoicify.invoice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class InvoiceDetails
{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    Long invoiceId;
    @ManyToOne()
    Items lineItem;
    Double totalPrice;

    public InvoiceDetails(Items lineItem, Double totalPrice) {
        this.lineItem = lineItem;
        this.totalPrice = totalPrice;
    }
}

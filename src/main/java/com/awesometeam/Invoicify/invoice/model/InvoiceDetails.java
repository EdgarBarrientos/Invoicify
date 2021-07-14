package com.awesometeam.Invoicify.invoice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class InvoiceDetails
{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    long id;
    long invoiceId;
    @OneToOne
    Items lineItem;
    Double totalPrice;

    public InvoiceDetails(long invoiceId, Items lineItem, Double totalPrice) {
        this.lineItem = lineItem;
        this.totalPrice = totalPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Items getLineItem() {
        return lineItem;
    }

    public void setLineItem(Items lineItem) {
        this.lineItem = lineItem;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}

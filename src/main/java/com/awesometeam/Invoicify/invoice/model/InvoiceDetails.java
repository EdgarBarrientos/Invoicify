package com.awesometeam.Invoicify.invoice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InvoiceDetails
{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    long id;
    long invoiceId;
    @OneToOne
    Items lineItem;
    double totalPrice;

    public InvoiceDetails(long invoiceId, Items lineItem, double totalPrice) {
       this.invoiceId=invoiceId;
        this.lineItem = lineItem;
        this.totalPrice = totalPrice;
    }
    @JsonProperty("Id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    @JsonProperty("InvoiceId")
    public long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
    }
    @JsonProperty("Items")
    public Items getLineItem() {
        return lineItem;
    }

    public void setLineItem(Items lineItem) {
        this.lineItem = lineItem;
    }

    @JsonProperty("TotalPrice")
    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}

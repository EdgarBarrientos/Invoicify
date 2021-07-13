package com.awesometeam.Invoicify.invoice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Items
{
    @Id
    @GeneratedValue (strategy= GenerationType.AUTO)
    long id;
    String description;
    char feeType;
    int quantity;
    Double fee;
    Double amount;


    public Items(String description, char feeType, int quantity, Double fee, Double amount) {
        this.description = description;
        this.feeType = feeType;
        this.quantity = quantity;
        this.fee = fee;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public char getFeeType() {
        return feeType;
    }

    public void setFeeType(char feeType) {
        this.feeType = feeType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

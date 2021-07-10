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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Items
{
    @Id
    @GeneratedValue (strategy= GenerationType.AUTO)
    Long id;
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
}

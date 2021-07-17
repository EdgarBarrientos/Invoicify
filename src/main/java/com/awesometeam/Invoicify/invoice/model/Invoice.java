package com.awesometeam.Invoicify.invoice.model;

import com.awesometeam.Invoicify.company.model.Company;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long invoiceId;
    @OneToOne
    Company company;
    LocalDate invoiceDate;
    String Status;
    LocalDate modifiedDate;
    double cost;
    @OneToMany(mappedBy = "invoiceId" )
    List<InvoiceDetails> invoiceDetailsList;

    public Invoice(Company company, LocalDate invoiceDate, String Status, LocalDate modifiedDate, double cost, List<InvoiceDetails> invoiceDetailsList) {
        this.company = company;
        this.invoiceDate = invoiceDate;
        this.Status = Status;
        this.modifiedDate = modifiedDate;
        this.cost = cost;
        this.invoiceDetailsList = invoiceDetailsList;
    }

    public Invoice(long invoiceId, Company company, LocalDate invoiceDate, String status, LocalDate modifiedDate, double cost, List<InvoiceDetails> invoiceDetailsList) {
        this.invoiceId = invoiceId;
        this.company = company;
        this.invoiceDate = invoiceDate;
        Status = status;
        this.modifiedDate = modifiedDate;
        this.cost = cost;
        this.invoiceDetailsList = invoiceDetailsList;
    }
}

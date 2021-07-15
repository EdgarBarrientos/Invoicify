package com.awesometeam.Invoicify.invoice.model;

import com.awesometeam.Invoicify.company.model.Company;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long invoiceId;
    @OneToOne
    Company company;
    Date invoiceDate;
    String Status;
    Date modifiedDate;
    double cost;
    @OneToMany(mappedBy = "invoiceId" )
    List<InvoiceDetails> invoiceDetailsList;

    public Invoice(Company company, Date invoiceDate, String Status, Date modifiedDate, double cost, List<InvoiceDetails> invoiceDetailsList) {
        this.company = company;
        this.invoiceDate = invoiceDate;
        this.Status = Status;
        this.modifiedDate = modifiedDate;
        this.cost = cost;
        this.invoiceDetailsList = invoiceDetailsList;
    }
}
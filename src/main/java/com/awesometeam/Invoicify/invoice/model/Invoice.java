package com.awesometeam.Invoicify.invoice.model;

import com.awesometeam.Invoicify.company.model.Company;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@JsonIgnoreProperties(ignoreUnknown = true)
public class Invoice implements Serializable {

    private static final long serialVersionUID = -3541482387808132540L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long invoiceId;
    @OneToOne
    Company company;
    LocalDate invoiceDate;
    String Status;
    LocalDate modifiedDate;
    double cost;
    @OneToMany(mappedBy = "invoiceId", fetch = FetchType.EAGER, orphanRemoval = true)
    List<InvoiceDetails> invoiceDetailsList;

    public Invoice(Company company, LocalDate invoiceDate, String Status, LocalDate modifiedDate, double cost, List<InvoiceDetails> invoiceDetailsList) {
        this.company = company;
        this.invoiceDate = invoiceDate;
        this.Status = Status;
        this.modifiedDate = modifiedDate;
        this.cost = cost;
        this.invoiceDetailsList = invoiceDetailsList;
    }
}

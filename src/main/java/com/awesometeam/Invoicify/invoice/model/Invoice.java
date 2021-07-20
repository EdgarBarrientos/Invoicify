package com.awesometeam.Invoicify.invoice.model;

import com.awesometeam.Invoicify.company.model.Company;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
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

    @JsonProperty("InvoiceId")
    public long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
    }

    @JsonProperty("Company")
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @JsonProperty("InvoiceDate")
    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @JsonProperty("ModifiedDate")
    public LocalDate getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @JsonProperty("Cost")
    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @JsonProperty("InvoiceDetails")
    public List<InvoiceDetails> getInvoiceDetailsList() {
        return invoiceDetailsList;
    }

    public void setInvoiceDetailsList(List<InvoiceDetails> invoiceDetailsList) {
        this.invoiceDetailsList = invoiceDetailsList;
    }
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

package com.awesometeam.Invoicify.company.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Setter
@Getter
public class Company {

    public Company() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("Id")
    public long Id;

    @JsonProperty("Name")
    public String Name;

    @JsonProperty("Address")
    public String Address;

    @OneToOne
    @JsonProperty("Contact")
    public Contact Contact;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(Name, company.Name) && Objects.equals(Address, company.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(Name, Address);
    }

    public String toString() {
        return "Company{" +
                "Name='" + Name + '\'' +
                ", Address='" + Address + '\'' +
                ", Contact='" + Contact.toString() + '\'' +
                '}';
    }



}

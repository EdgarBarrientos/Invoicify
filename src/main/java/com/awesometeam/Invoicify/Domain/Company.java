package com.awesometeam.Invoicify.Domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.boot.registry.selector.spi.StrategySelectionException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Setter
@Getter
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long Id;

    public String Name;
    public String Address;
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
                "name='" + Name + '\'' +
                ", address='" + Address + '\'' +
                ", contact='" + Contact.toString() + '\'' +
                '}';
    }



}

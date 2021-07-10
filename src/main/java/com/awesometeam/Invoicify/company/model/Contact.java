package com.awesometeam.Invoicify.company.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Setter
@Getter
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long Id;

    public String Name;
    public String Title;
    public String PhoneNumber;

    public String toString() {
        return "Company{" +
                "name='" + Name + '\'' +
                ", title='" + Title + '\'' +
                ", phoneNumber='" + PhoneNumber +
                '}';
    }
}

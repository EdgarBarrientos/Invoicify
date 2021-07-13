package com.awesometeam.Invoicify.company.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("Id")
    public long Id;

    @JsonProperty("Name")
    public String Name;
    @JsonProperty("Title")
    public String Title;
    @JsonProperty("PhoneNumber")
    public String PhoneNumber;

    public String toString() {
        return "Company{" +
                "name='" + Name + '\'' +
                ", title='" + Title + '\'' +
                ", phoneNumber='" + PhoneNumber +
                '}';
    }
}

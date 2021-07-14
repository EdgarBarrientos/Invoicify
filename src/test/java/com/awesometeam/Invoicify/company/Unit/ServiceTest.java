package com.awesometeam.Invoicify.company.Unit;

import com.awesometeam.Invoicify.company.model.Company;
import com.awesometeam.Invoicify.company.model.Contact;
import com.awesometeam.Invoicify.company.repository.CompanyRepository;
import com.awesometeam.Invoicify.company.repository.ContactRepository;
import com.awesometeam.Invoicify.company.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {
    @Mock
    CompanyRepository companyRepository;
    @Mock
    ContactRepository contactRepository;
    @InjectMocks
    CompanyService companyService;

    @Test
    void testAddCompany(){
        Company company= new Company();
        company.Name= "Acme Inc.";
        company.Address="322 Hogestown, Mechanicsburg, PA ";

        Contact contact= new Contact();
        contact.Name ="Peter Lee";
        contact.Title="Associate";
        contact.PhoneNumber ="123456789";

        company.Contact=contact;

        Company companyDup= new Company();
        companyDup.Name= "Acme Inc.";
        companyDup.Address="324 Hogestown, Mechanicsburg, PA ";

        companyDup.Contact=contact;

        when(contactRepository.save(company.getContact())).thenReturn(company.getContact());
        //when(contactRepository.save(companyDup.getContact())).thenReturn(null);

        when(companyRepository.save(company)).thenReturn(company);
        //when(companyRepository.save(companyDup)).thenReturn(null);

        Company currentResult=companyService.Add(company);
        assertEquals(currentResult,company);

        //Company currentResult2=companyService.Add(companyDup);
        //assertNull(currentResult2);
    }

    @Test
    void getAllCompaniesTest() {
        List<Company> companies = new ArrayList<>();
        Company company = new Company();
        company.setId(1l);
        company.setName("Walmart");
        company.setAddress("Greenspring Street");
        Contact contact = new Contact();
        contact.Name ="Peter Lee";
        contact.Title="Associate";
        contact.PhoneNumber ="123456789";
        company.setContact(contact);
        companies.add(company);
        when(companyRepository.findAll()).thenReturn(companies);
        List<Company> actual = companyService.findAll();
        assertEquals(companies,actual);

    }

    @Test
    void updateCompaniesTest() {
        Company company = new Company();
        company.setId(1);
        company.setName("Walmart");
        company.setAddress("Greenspring Street");
        Contact contact = new Contact();
        contact.Name ="Peter Lee";
        contact.Title="Associate";
        contact.PhoneNumber ="123456789";
        company.setContact(contact);

        when(companyRepository.save(company)).thenReturn(company);
        when(contactRepository.save(company.getContact())).thenReturn(company.getContact());
        when(companyRepository.findById(company.getId())).thenReturn(java.util.Optional.of(company));
        Company actual = companyService.Update(company);
        assertEquals(company,actual);

    }

}

package com.awesometeam.Invoicify.Unit;

import com.awesometeam.Invoicify.Domain.Company;
import com.awesometeam.Invoicify.Domain.Contact;
import com.awesometeam.Invoicify.Repository.CompanyRepository;
import com.awesometeam.Invoicify.Service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {
    @Mock
    CompanyRepository companyRepository;
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

        when(companyRepository.save(company)).thenReturn(company);

        Company currentResult=companyService.Add(company);
        assertEquals(currentResult,company);
    }

}

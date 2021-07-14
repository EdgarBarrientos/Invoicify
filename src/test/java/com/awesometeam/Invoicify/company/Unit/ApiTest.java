package com.awesometeam.Invoicify.company.Unit;

import com.awesometeam.Invoicify.company.model.Company;
import com.awesometeam.Invoicify.company.model.Contact;
import com.awesometeam.Invoicify.company.service.CompanyService;
import com.awesometeam.Invoicify.invoice.service.InvoiceDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest()
public class ApiTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CompanyService service;

    @MockBean
    InvoiceDetailsService invoiceDetailsService;

    @Test
    void  checkAddCompany(  ) throws Exception{
        Company adding= new Company();
        adding.Name= "Acme Inc.";
        adding.Address="322 Hogestown, Mechanicsburg, PA ";

        Contact contact= new Contact();
        contact.Name ="Peter Lee";
        contact.Title="Associate";
        contact.PhoneNumber ="123456789";

        adding.Contact=contact;

        doAnswer(invocation -> {
            Company p = invocation.getArgument(0);
            p.setId(1);
            return p;
        }).when(service).Add(isA(Company.class));

        Map<String, Object> body = new HashMap<>();
        body.put("Name", adding.getName());
        body.put("Address", adding.getAddress());
        body.put("Contact", adding.getContact());

        mvc.perform(post("/company")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("Id").value(1))
                .andExpect(jsonPath("Name").value("Acme Inc."))
                .andExpect(jsonPath("Address").value("322 Hogestown, Mechanicsburg, PA "));

    }

    @Test
    void getAllCompaniesTest() throws Exception {
        List<Company> companies = new ArrayList<>();
        Company company = new Company();
        company.setId(1l);
        company.setName("Walmart");
        company.setAddress("Greenspring Street");
        Contact contact = new Contact();
        contact.Name = "Peter Lee";
        contact.Title = "Associate";
        contact.PhoneNumber = "123456789";
        company.setContact(contact);
        companies.add(company);


        when(service.findAll()).thenReturn(companies);

        mvc.perform(get("/company"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("[0]").value(companies.get(0)));
    }

    void TestCompanyUpdateName () throws  Exception{
        Company updating= new Company();
        // assume that post method to save contact
        // contact already saved
        updating.Name= "Acme Inc.";
        updating.Address="322 Hogestown, Mechanicsburg, PA ";

        Contact contact= new Contact();
        contact.Name ="Peter Lee";
        contact.Title="Associate";
        contact.PhoneNumber ="123456789";

        updating.Contact=contact;
        doAnswer(invocation -> {
            Company p = invocation.getArgument(0);
            Long index = p.getId();
            Company result = null;
            if (index == 1) {
                result = new Company(updating.getName(), updating.getAddress(), updating.getContact());
                result.setId(1);
                if (p.getName() != null)
                    result.setName(p.getName());
                if (p.getAddress() != null)
                    result.setAddress(p.getAddress());
                if (p.getContact() != null)
                    result.setContact(p.getContact());
            }
            return Optional.ofNullable(result);
        }).when(service).Update(isA(Company.class));

        Map<String, Object> body = new HashMap<>();
        body.put("Name", updating.getName());
        body.put("Address", updating.getAddress());
        body.put("Contact", updating.getContact());

        mvc.perform(put("/company/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("Id").value(1))
                .andExpect(jsonPath("Name").value("Acme Inc."))
                .andExpect(jsonPath("Address").value("322 Hogestown, Mechanicsburg, PA "));

        mvc.perform(put("/company/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());

    }

}

package com.awesometeam.Invoicify.company.Unit;

import com.awesometeam.Invoicify.company.model.Company;
import com.awesometeam.Invoicify.company.model.Contact;
import com.awesometeam.Invoicify.company.service.CompanyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.HashMap;
import java.util.Map;

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
            return null;
        }).when(service).Add(isA(Company.class));

        Map<String, Object> body = new HashMap<>();
        body.put("name", adding.getName());
        body.put("address", adding.getAddress());
        body.put("contact", adding.getContact());

        mvc.perform(post("/company")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("name").value("Acme Inc."))
                .andExpect(jsonPath("address").value("322 Hogestown, Mechanicsburg, PA "));

    }


}

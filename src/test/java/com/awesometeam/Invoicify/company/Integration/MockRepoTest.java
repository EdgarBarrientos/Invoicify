package com.awesometeam.Invoicify.company.Integration;

import com.awesometeam.Invoicify.company.model.Company;
import com.awesometeam.Invoicify.company.model.Contact;
import com.awesometeam.Invoicify.company.repository.CompanyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
//@AutoConfigureRestDocs
public class MockRepoTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CompanyRepository repo;


    @Test
    void addCompany() throws Exception {
        Company adding = new Company();
        adding.Name = "Acme Inc.";
        adding.Address = "322 Hogestown, Mechanicsburg, PA ";

        Contact contact = new Contact();
        contact.Name = "Peter Lee";
        contact.Title = "Associate";
        contact.PhoneNumber = "123456789";

        adding.Contact = contact;



        doAnswer(invocation -> {
            Company p = invocation.getArgument(0);
            p.setId(1);
            return null;
        }).when(repo).save(isA(Company.class));

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
               /* .andDo(document("company POST"
                        , requestFields(
                                fieldWithPath("name").description("The name of the company being added"),
                                fieldWithPath("address").description("The address of the company being added"))
                        , responseFields(
                                fieldWithPath("id").description("Internal ID of the added company. For use with POST"),
                                fieldWithPath("name").description("The name of the added company"),
                                fieldWithPath("address").description("The address of the added company"))
                        )
                )*/

    }
}
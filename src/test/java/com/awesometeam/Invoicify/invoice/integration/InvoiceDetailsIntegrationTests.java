package com.awesometeam.Invoicify.invoice.integration;


import com.awesometeam.Invoicify.invoice.model.InvoiceDetails;
import com.awesometeam.Invoicify.invoice.model.Items;
import com.awesometeam.Invoicify.invoice.repository.InvoiceDetailsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.awesometeam.Invoicify.invoice.utility.Helper.getJSON;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.awesometeam.Invoicify.invoice.model.*;
import com.awesometeam.Invoicify.invoice.controller.InvoiceController;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class InvoiceDetailsIntegrationTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    InvoiceDetailsRepository repo;

    @Test
    void addNewLineItemTest() throws Exception {
        List<Items> itemsList = new ArrayList<>();
        itemsList.add(new Items(1L, "item1", 'F', 0, 0.0, 20.0));
        itemsList.add(new Items(2L, "item2", 'R', 10, 5.0, 0.0));

        InvoiceDetails invoiceDetails = new InvoiceDetails(itemsList.get(0), itemsList.get(0).getAmount());
        System.out.println(itemsList.get(0).getId());
        String json = getJSON("src/test/resources/InvoiceLineItem.json");

        doAnswer(invocation -> {
            InvoiceDetails invoiceDetails1 = invocation.getArgument(0);
            invoiceDetails1.setInvoiceId(1L);
            return null;
        }).when(repo).save(isA(InvoiceDetails.class));
        this.mvc.perform(post("/addInvoiceItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andDo(document("Add Invoice Item"
                        , requestFields(
                                fieldWithPath("lineItem.description").description(""),
                                fieldWithPath("lineItem.feeType").description(""),
                                fieldWithPath("lineItem.quantity").description(""),
                                fieldWithPath("lineItem.fee").description(""),
                                fieldWithPath("lineItem.amount").description(""),
                                fieldWithPath("totalPrice").description(""))
                        , responseFields(
                                fieldWithPath("id").description("Internal ID of the added pupper. For use with PATCH"),
                                fieldWithPath("name").description("The name of the added pupper"),
                                fieldWithPath("breed").description("The breed name of the added pupper"))
                        )
                );
    }
}

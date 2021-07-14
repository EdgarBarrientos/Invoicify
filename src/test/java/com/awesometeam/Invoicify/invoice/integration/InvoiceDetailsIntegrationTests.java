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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.awesometeam.Invoicify.invoice.utility.Helper.getJSON;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        itemsList.add(new Items(1,"item1", 'F', 0, 1.0, 20.0));
        //itemsList.add(new Items("item2", 'R', 10, 5.0, 0.0));
        Items item = new Items(1,"item1", 'F', 0, 1.0, 20.0);
        InvoiceDetails invoiceDetails2 = new InvoiceDetails();
        invoiceDetails2.setInvoiceId(1);
        invoiceDetails2.setLineItem(item);
        invoiceDetails2.setTotalPrice(20.0);

        Map<String,Object> itemMap = new HashMap<>();
        Map<String,Object> invoiceMap = new HashMap<>();
        itemMap.put("id", 1);
        itemMap.put("description", "item1");
        itemMap.put("feeType", "F");
        itemMap.put("quantity", 0);
        itemMap.put("fee", 1.0);
        itemMap.put("amount", 20.0);
        invoiceMap.put("invoiceId",1);
        invoiceMap.put("lineItem",itemMap);
        invoiceMap.put("totalPrice","20");

        // InvoiceDetails invoiceDetails = new InvoiceDetails(itemsList.get(0), itemsList.get(0).getAmount());
        // String json = getJSON("src/test/resources/InvoiceLineItem.json");

        doAnswer(invocation -> {
            InvoiceDetails invoiceDetails1 = invocation.getArgument(0);

            invoiceDetails1.setId(1);

            invoiceDetails1.setInvoiceId(1);


            return invoiceDetails1;
        }).when(repo).save(isA(InvoiceDetails.class));
        this.mvc.perform(post("/addInvoiceItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invoiceMap)))
                // .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("totalPrice").value(20))
                // .andExpect(jsonPath("$.lineItem.description").value("item1"))
//                .andExpect(jsonPath("feeType").value("F"))
//                .andExpect(jsonPath("fee").value("1L"))
//                .andExpect(jsonPath("amount").value("20"))
                .andDo(document("Add Invoice Item POST"
                        , requestFields(
                                fieldWithPath("lineItem").description("Line Item details"),
                                fieldWithPath("invoiceId").description("invoice to which the Line Item details added"),
                                fieldWithPath("lineItem.id").description("Internal ID of the added line item"),
                                fieldWithPath("lineItem.description").description("Description of the line Item"),
                                fieldWithPath("lineItem.feeType").description("Fee type is either Flat Fee or Rate based fee"),
                                fieldWithPath("lineItem.quantity").description("Line item quantity"),
                                fieldWithPath("lineItem.fee").description("Line item fee. This is Flat Fee/ Rate based fee"),
                                fieldWithPath("lineItem.amount").description("This is line item amount/price"),
                                fieldWithPath("totalPrice").description("Total price of the line item"))
                        , responseFields(
                                fieldWithPath("id").description("Internal ID of the added invoice"),
                                fieldWithPath("invoiceId").description("invoice to which the Line Item details added"),
                                fieldWithPath("lineItem").description("Line Item details"),
                                fieldWithPath("lineItem.id").description("Internal ID of the added line item"),
                                fieldWithPath("lineItem.description").description("Description of the line Item"),
                                fieldWithPath("lineItem.feeType").description("Fee type is either Flat Fee or Rate based fee"),
                                fieldWithPath("lineItem.quantity").description("Line item quantity"),
                                fieldWithPath("lineItem.fee").description("Line item fee. This is Flat Fee/ Rate based fee"),
                                fieldWithPath("lineItem.amount").description("This is line item amount/price"),
                                fieldWithPath("totalPrice").description("Total price of the line item"))
                        )
                );
    }
}
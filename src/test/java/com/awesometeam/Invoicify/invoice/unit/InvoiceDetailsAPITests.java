package com.awesometeam.Invoicify.invoice.unit;

import com.awesometeam.Invoicify.company.service.CompanyService;
import com.awesometeam.Invoicify.invoice.controller.InvoiceController;
import com.awesometeam.Invoicify.invoice.model.InvoiceDetails;
import com.awesometeam.Invoicify.invoice.model.Items;
import com.awesometeam.Invoicify.invoice.service.InvoiceDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.awesometeam.Invoicify.invoice.utility.Helper.getJSON;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InvoiceController.class)
public class InvoiceDetailsAPITests
{
    @Autowired
    private MockMvc mvc;

    @MockBean
    InvoiceDetailsService service;

    @MockBean
    CompanyService companyService;

    @Test
    void addNewLineItemsTest() throws Exception
    {
        List<Items> itemsList = new ArrayList<>();
        itemsList.add (new Items(1L,"item1",'F',0,0.0,20.0));
        itemsList.add (new Items(2L,"item2",'R',10,5.0,0.0));

        InvoiceDetails invoiceDetails = new InvoiceDetails(itemsList.get(0),itemsList.get(0).getAmount());

        String json = getJSON("src/test/resources/InvoiceLineItem.json");

        doAnswer(invocation -> {
            InvoiceDetails invoiceDetails1 = invocation.getArgument(0);
            invoiceDetails1.setInvoiceId(1L);
            return invoiceDetails1;
        }).when(service).addNewLineItem(isA(InvoiceDetails.class));

        //when(service.addNewLineItem(invoiceDetails)).thenReturn(invoiceDetails);
        this.mvc.perform(post("/addInvoiceItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());


    }

}

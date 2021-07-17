package com.awesometeam.Invoicify.invoice.integration;

import com.awesometeam.Invoicify.company.model.Company;
import com.awesometeam.Invoicify.company.model.Contact;
import com.awesometeam.Invoicify.invoice.dto.DtoInvoiceDetails;
import com.awesometeam.Invoicify.invoice.model.Invoice;
import com.awesometeam.Invoicify.invoice.model.InvoiceDetails;
import com.awesometeam.Invoicify.invoice.model.Items;
import com.awesometeam.Invoicify.invoice.repository.InvoiceDetailsRepository;
import com.awesometeam.Invoicify.invoice.repository.InvoiceRepository;
import com.awesometeam.Invoicify.invoice.repository.ItemsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs

public class InvoiceIntegrationTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    InvoiceRepository invoiceRepository;

    @MockBean
    InvoiceDetailsRepository repo;

    @MockBean
    ItemsRepository itemsRepository;

    @Test
    void createNewInvoice() throws Exception{
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice invoice=new Invoice(company, LocalDate.of(2021,07,12)
                ,"Unpaid",LocalDate.of (2021,07,12) ,0.0, null );
        Map<String, Object> requestBody= new HashMap<>();
        requestBody.put("company", invoice.getCompany());
        requestBody.put("invoiceDate", invoice.getInvoiceDate());
        requestBody.put("status", invoice.getStatus());        requestBody.put("modifiedDate", invoice.getModifiedDate());
        requestBody.put("cost", invoice.getCost());
        requestBody.put("invoiceDetails", null);

        doAnswer(invocation ->{
            Invoice inv=invocation.getArgument(0);
            inv.setInvoiceId(1);
            return inv;
        }).when(invoiceRepository).save(isA(Invoice.class));

        mvc.perform(post("/createNewInvoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("invoiceId").value(1))
                .andExpect(jsonPath("cost").value(0.0));
//                .andDo(document("Add Invoice POST"
//                                , requestFields(
//                                        fieldWithPath("company").description("Line Item details"),
//                                        fieldWithPath("company.Id").description("invoice to which the Line Item details added"),
//                                        fieldWithPath("company.Name").description("Internal ID of the added line item"),
//                                        fieldWithPath("company.Address").description("Description of the line Item"),
//                                        fieldWithPath("company.Contact").description("Fee type is either Flat Fee or Rate based fee"),
//                                        fieldWithPath("company.Contact.Id").description("Line item quantity"),
//                                        fieldWithPath("company.Contact.Name").description("Line item fee. This is Flat Fee/ Rate based fee"),
//                                        fieldWithPath("company.Contact.Title").description("This is line item amount/price"),
//                                        fieldWithPath("company.Contact.PhoneNumber").description("Total price of the line item"),
//                                        fieldWithPath("invoiceDate").description("Total price of the line item"),
//                                        fieldWithPath("modifiedDate").description("Total price of the line item"),
//                                        fieldWithPath("cost").description("Total price of the line item"),
//                                        fieldWithPath("invoiceDetailsList").description("Total price of the line item"),
//                                fieldWithPath("invoiceDetails").description("Total price of the line item"),
//                                        fieldWithPath("invoiceDetails.invoiceId").description("invoice to which the Line Item details added"),
//                                fieldWithPath("invoiceDetails.id").description("invoice to which the Line Item details added"),
//                                fieldWithPath("invoiceDetails.lineItem.id").description("Internal ID of the added line item"),
//                                fieldWithPath("invoiceDetails.lineItem.description").description("Description of the line Item"),
//                                fieldWithPath("invoiceDetails.lineItem.feeType").description("Fee type is either Flat Fee or Rate based fee"),
//                                fieldWithPath("invoiceDetails.lineItem.quantity").description("Line item quantity"),
//                                fieldWithPath("invoiceDetails.lineItem.fee").description("Line item fee. This is Flat Fee/ Rate based fee"),
//                                fieldWithPath("invoiceDetails.lineItem.amount").description("This is line item amount/price"),
//                                fieldWithPath("invoiceDetails.totalPrice").description("Total price of the line item"),
//
//                                       fieldWithPath("status").description("Total price of the line item")),
//
//                                        responseFields(
//                                        fieldWithPath("invoiceId").description("Internal ID of the added invoice"),
//                fieldWithPath("company").description("Line Item details"),
//                fieldWithPath("company.Id").description("invoice to which the Line Item details added"),
//                fieldWithPath("company.Name").description("Internal ID of the added line item"),
//                fieldWithPath("company.Address").description("Description of the line Item"),
//                fieldWithPath("company.Contact").description("Fee type is either Flat Fee or Rate based fee"),
//                fieldWithPath("company.Contact.Id").description("Line item quantity"),
//                fieldWithPath("company.Contact.Name").description("Line item fee. This is Flat Fee/ Rate based fee"),
//                fieldWithPath("company.Contact.Title").description("This is line item amount/price"),
//                fieldWithPath("company.Contact.PhoneNumber").description("Total price of the line item"),
//        fieldWithPath("invoiceDate").description("Total price of the line item"),
//        fieldWithPath("modifiedDate").description("Total price of the line item"),
//        fieldWithPath("cost").description("Total price of the line item"),
//        fieldWithPath("invoiceDetailsList").description("Total price of the line item"),
//                                                fieldWithPath("invoiceDetails").description("Total price of the line item"),
//
//                                                fieldWithPath("invoiceDetails.invoiceId").description("invoice to which the Line Item details added"),
//                                                fieldWithPath("invoiceDetails.id").description("invoice to which the Line Item details added"),
//                                                fieldWithPath("invoiceDetails.lineItem.id").description("Internal ID of the added line item"),
//                                                fieldWithPath("invoiceDetails.lineItem.description").description("Description of the line Item"),
//                                                fieldWithPath("invoiceDetails.lineItem.feeType").description("Fee type is either Flat Fee or Rate based fee"),
//                                                fieldWithPath("invoiceDetails.lineItem.quantity").description("Line item quantity"),
//                                                fieldWithPath("invoiceDetails.lineItem.fee").description("Line item fee. This is Flat Fee/ Rate based fee"),
//                                                fieldWithPath("invoiceDetails.lineItem.amount").description("This is line item amount/price"),
//                                                fieldWithPath("invoiceDetails.totalPrice").description("Total price of the line item"),
//                                       fieldWithPath("status").description("Total price of the line item")
//                        )));
    }

    @Test
    void addNewLineItemTestWithFlatFee() throws Exception {
        List<Items> itemsList = new ArrayList<>();
        itemsList.add (new Items(1,"item1",'F',0,0.0,20.0));
        itemsList.add (new Items(2,"item2",'F',0,0.0,20.0));

        InvoiceDetails invoiceDetails = new InvoiceDetails(1, itemsList.get(0),itemsList.get(0).getAmount());
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice invoice=new Invoice(company, LocalDate.of(2021,07,12)
                ,"Unpaid",LocalDate.of (2021,07,12) ,0.0, null );


        Map<String, Object> requestBody= new HashMap<>();
        requestBody.put("invoiceId", invoiceDetails.getInvoiceId());
        requestBody.put("lineItem", invoiceDetails.getLineItem());
        requestBody.put("totalPrice", invoiceDetails.getTotalPrice());

        doAnswer(invocation -> {
            Invoice invoice1 = invocation.getArgument(0);
            invoice1.setInvoiceId(1);
            return invoice1;
        }).when(invoiceRepository).save(isA(Invoice.class));

        doAnswer(invocation -> {
            InvoiceDetails invoiceDetails1 = invocation.getArgument(0);
            invoiceDetails1.setId(1);
            invoiceDetails1.setInvoiceId(1);
            return invoiceDetails1;
        }).when(repo).save(isA(InvoiceDetails.class));

        this.mvc.perform(post("/addInvoiceItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("totalPrice").value(20));
//                .andDo(document("Add Invoice Item POST"
//                        , requestFields(
//                                fieldWithPath("lineItem").description("Line Item details"),
//                                fieldWithPath("invoiceId").description("invoice to which the Line Item details added"),
//                                fieldWithPath("lineItem.id").description("Internal ID of the added line item"),
//                                fieldWithPath("lineItem.description").description("Description of the line Item"),
//                                fieldWithPath("lineItem.feeType").description("Fee type is either Flat Fee or Rate based fee"),
//                                fieldWithPath("lineItem.quantity").description("Line item quantity"),
//                                fieldWithPath("lineItem.fee").description("Line item fee. This is Flat Fee/ Rate based fee"),
//                                fieldWithPath("lineItem.amount").description("This is line item amount/price"),
//                                fieldWithPath("totalPrice").description("Total price of the line item"))
//                        , responseFields(
//                                fieldWithPath("id").description("Internal ID of the added invoice"),
//                                fieldWithPath("invoiceId").description("invoice to which the Line Item details added"),
//                                fieldWithPath("lineItem").description("Line Item details"),
//                                fieldWithPath("lineItem.id").description("Internal ID of the added line item"),
//                                fieldWithPath("lineItem.description").description("Description of the line Item"),
//                                fieldWithPath("lineItem.feeType").description("Fee type is either Flat Fee or Rate based fee"),
//                                fieldWithPath("lineItem.quantity").description("Line item quantity"),
//                                fieldWithPath("lineItem.fee").description("Line item fee. This is Flat Fee/ Rate based fee"),
//                                fieldWithPath("lineItem.amount").description("This is line item amount/price"),
//                                fieldWithPath("totalPrice").description("Total price of the line item"))
//                        )
//                );
    }

    @Test
    void addNewLineItemTestWithRate() throws Exception {
        List<Items> itemsList = new ArrayList<>();
        itemsList.add (new Items(1,"item1",'R',5,10.0,0.0));
        itemsList.add (new Items(2,"item2",'R',5,20.0,0.0));

        InvoiceDetails invoiceDetails = new InvoiceDetails(1, itemsList.get(0),itemsList.get(0).getQuantity() * itemsList.get(0).getFee());
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice invoice=new Invoice(company, LocalDate.of(2021,07,12)
                ,"Unpaid",LocalDate.of (2021,07,12) ,0.0, null );

        Map<String, Object> requestBody= new HashMap<>();
        requestBody.put("invoiceId", invoiceDetails.getInvoiceId());
        requestBody.put("lineItem", invoiceDetails.getLineItem());
        requestBody.put("totalPrice", invoiceDetails.getTotalPrice());

        doAnswer(invocation -> {
            Invoice invoice1 = invocation.getArgument(0);
            invoice1.setInvoiceId(1);
            return invoice1;
        }).when(invoiceRepository).save(isA(Invoice.class));

        doAnswer(invocation -> {
            InvoiceDetails invoiceDetails1 = invocation.getArgument(0);
            invoiceDetails1.setId(1);
            invoiceDetails1.setInvoiceId(1);
            return invoiceDetails1;
        }).when(repo).save(isA(InvoiceDetails.class));

        this.mvc.perform(post("/addInvoiceItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("totalPrice").value(50));
//                .andDo(document("Add Invoice Item POST"
//                        , requestFields(
//                                fieldWithPath("lineItem").description("Line Item details"),
//                                fieldWithPath("invoiceId").description("invoice to which the Line Item details added"),
//                                fieldWithPath("lineItem.id").description("Internal ID of the added line item"),
//                                fieldWithPath("lineItem.description").description("Description of the line Item"),
//                                fieldWithPath("lineItem.feeType").description("Fee type is either Flat Fee or Rate based fee"),
//                                fieldWithPath("lineItem.quantity").description("Line item quantity"),
//                                fieldWithPath("lineItem.fee").description("Line item fee. This is Flat Fee/ Rate based fee"),
//                                fieldWithPath("lineItem.amount").description("This is line item amount/price"),
//                                fieldWithPath("totalPrice").description("Total price of the line item"))
//                        , responseFields(
//                                fieldWithPath("id").description("Internal ID of the added invoice"),
//                                fieldWithPath("invoiceId").description("invoice to which the Line Item details added"),
//                                fieldWithPath("lineItem").description("Line Item details"),
//                                fieldWithPath("lineItem.id").description("Internal ID of the added line item"),
//                                fieldWithPath("lineItem.description").description("Description of the line Item"),
//                                fieldWithPath("lineItem.feeType").description("Fee type is either Flat Fee or Rate based fee"),
//                                fieldWithPath("lineItem.quantity").description("Line item quantity"),
//                                fieldWithPath("lineItem.fee").description("Line item fee. This is Flat Fee/ Rate based fee"),
//                                fieldWithPath("lineItem.amount").description("This is line item amount/price"),
//                                fieldWithPath("totalPrice").description("Total price of the line item"))
//                        )
//                );
    }

    @Test
    void findInvoiceByInvoiceIdTest() throws Exception{
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice invoice=new Invoice(1,company, LocalDate.of(2021,07,12)
                ,"Unpaid",LocalDate.of (2021,07,12) ,1.0, null );

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        mvc.perform(get("/invoice/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("invoiceId").value(1))
                .andExpect(jsonPath("status").value("Unpaid"))
                .andExpect(jsonPath("$.company.Name").value("ABC..inc"))
                .andExpect(jsonPath("cost").value(1.0));
   }

    @Test
    void findUnpaidInvoiceByCompany() throws Exception {
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice invoice=new Invoice(company,  LocalDate.of(2021,07,21)
                ,"Unpaid", LocalDate.of (2021,07,12) ,10.0, null );
        Invoice invoice2=new Invoice(company,  LocalDate.of(2021,07,12)
                ,"Unpaid", LocalDate.of (2021,07,21) ,15.0, null );


        List<Items> itemsListA = new ArrayList<>();
        itemsListA.add (new Items("item1",'F',0,0.0,20.0));
        itemsListA.add (new Items("item2",'R',10,5.0,0.0));

        List<Items> itemsListB = new ArrayList<>();
        itemsListB.add (new Items("item3",'F',0,0.0,20.0));
        itemsListB.add (new Items("item4",'R',10,5.0,0.0));

        InvoiceDetails invoiceDetailsA = new InvoiceDetails(1, itemsListA.get(0),itemsListA.get(0).getAmount());
        InvoiceDetails invoiceDetailsB = new InvoiceDetails(2, itemsListB.get(0),itemsListB.get(0).getAmount());
        InvoiceDetails invoiceDetailsC = new InvoiceDetails(1, itemsListA.get(1),itemsListA.get(1).getAmount());
        InvoiceDetails invoiceDetailsD = new InvoiceDetails(2, itemsListB.get(1),itemsListB.get(1).getAmount());
        invoice.setInvoiceDetailsList(Arrays.asList(invoiceDetailsA, invoiceDetailsC));
        invoice2.setInvoiceDetailsList(Arrays.asList(invoiceDetailsB, invoiceDetailsD));

        List<Invoice> listOfInvoices = new ArrayList<>();
        listOfInvoices.add(invoice);
        listOfInvoices.add(invoice2);
        DtoInvoiceDetails dtoInvoiceDetail = new DtoInvoiceDetails();
        dtoInvoiceDetail = dtoInvoiceDetail.mapInvoiceDetails(invoice);
        DtoInvoiceDetails dtoInvoiceDetail2 = new DtoInvoiceDetails();
        dtoInvoiceDetail2 = dtoInvoiceDetail2.mapInvoiceDetails(invoice2);
        List<DtoInvoiceDetails> dtoInvoiceDetails = new ArrayList<>();
        dtoInvoiceDetails.add(dtoInvoiceDetail);
        dtoInvoiceDetails.add(dtoInvoiceDetail2);

        Pageable paging = PageRequest.of(0, 10, Sort.by("invoiceDate").ascending());
        Page page = new PageImpl(listOfInvoices, paging, 10);
        when(invoiceRepository.findByCompanyIdAndStatus(1L, "Unpaid", paging)).thenReturn(page);

            mvc.perform(get("/invoiceByCompany/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(dtoInvoiceDetails.get(0).getId().toString()))
                .andExpect(jsonPath("$.content[0].createDate").value(dtoInvoiceDetails.get(0).getCreateDate().toString()))
                .andExpect(jsonPath("$.content[0].total").value(dtoInvoiceDetails.get(0).getTotal()))
                .andExpect(jsonPath("$.content[0].paidStatus").value(dtoInvoiceDetails.get(0).getPaidStatus()))
                .andExpect(jsonPath("$.content[0].itemsDetails[0].lineItem.description").value("item1"))
                .andExpect(jsonPath("$.content[0].itemsDetails[0].lineItem.feeType").value("F"))
                .andExpect(jsonPath("$.content[0].itemsDetails[0].lineItem.quantity").value(0))
                .andExpect(jsonPath("content.length()").value(listOfInvoices.size()));

    }

}

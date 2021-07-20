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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.anyList;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void createNewInvoiceWithNoItems() throws Exception{
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice invoice=new Invoice(company, LocalDate.of(2021,07,12)
                ,"Unpaid",LocalDate.of (2021,07,12) ,0.0, null );
        Map<String, Object> requestBody= new HashMap<>();
        requestBody.put("Company", invoice.getCompany());
        requestBody.put("InvoiceDate", invoice.getInvoiceDate());
        requestBody.put("Status", invoice.getStatus());
        requestBody.put("ModifiedDate", invoice.getModifiedDate());
        requestBody.put("Cost", invoice.getCost());
        requestBody.put("InvoiceDetails", null);

        doAnswer(invocation ->{
            Invoice inv=invocation.getArgument(0);
            inv.setInvoiceId(1);
            return inv;
        }).when(invoiceRepository).save(isA(Invoice.class));

        mvc.perform(post("/createNewInvoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("InvoiceId").value(1))
                .andExpect(jsonPath("Cost").value(0.0));
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
//                                        fieldWithPath("status").description("Total price of the line item"),
//                                        fieldWithPath("invoiceDetails[].totalPrice").description("Total price of the line item"),
//                                        fieldWithPath("invoiceDetails[].items").description("Total price of the line item"),
//                                        fieldWithPath("invoiceDetails[].id").description("Total price of the line item"),
//                                fieldWithPath("invoiceDetails[].items.id").description("Total price of the line item"),
//                                fieldWithPath("invoiceDetails[].items.description").description("Total price of the line item"),
//                                fieldWithPath("invoiceDetails[].items.feeType").description("Total price of the line item"),
//                                fieldWithPath("invoiceDetails[].items.quantity").description("Total price of the line item"),
//                                fieldWithPath("invoiceDetails[].items.fee").description("Total price of the line item"),
//                                fieldWithPath("invoiceDetails[].items.amount").description("Total price of the line item"),
//
//
//
//                                fieldWithPath("cost").description("Total price of the line item")),
//                                                                                responseFields(
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
//        fieldWithPath("cost").description("Total price of the line item"))));
//
    }

    @Test
    void createNewInvoiceWithItems() throws Exception{
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        List<Items> itemsList = new ArrayList<>();
        itemsList.add (new Items(1,"item1",'R',5,10.0,0.0));
        itemsList.add (new Items(2,"item2",'F',0,0.0,20.0));
        List<InvoiceDetails> invoiceDetailsList = new ArrayList<>();
        invoiceDetailsList.add(new InvoiceDetails(1, itemsList.get(0),itemsList.get(0).getQuantity() * itemsList.get(0).getFee()));
        invoiceDetailsList.add(new InvoiceDetails(1, itemsList.get(1),itemsList.get(1).getAmount()));
        Invoice invoice=new Invoice(company, LocalDate.of(2021,07,12)
                ,"Unpaid",LocalDate.of (2021,07,12) ,70.0, invoiceDetailsList );
        Map<String, Object> requestBody= new HashMap<>();
        requestBody.put("Company", invoice.getCompany());
        requestBody.put("InvoiceDate", invoice.getInvoiceDate());
        requestBody.put("Status", invoice.getStatus());
        requestBody.put("ModifiedDate", invoice.getModifiedDate());
        requestBody.put("Cost", invoice.getCost());
        requestBody.put("InvoiceDetails", invoiceDetailsList);

        doAnswer(invocation ->{
            List <InvoiceDetails> invoiceDetailsList1=invocation.getArgument(0);
            invoiceDetailsList1.get(0).setId(1);
            invoiceDetailsList1.get(1).setId(2);
            invoiceDetailsList1.get(0).setInvoiceId(1);
            invoiceDetailsList1.get(1).setInvoiceId(1);
            return invoiceDetailsList1;
        }).when(repo).saveAll(anyList());

        doAnswer(invocation ->{
            Invoice inv=invocation.getArgument(0);
            inv.setInvoiceId(1);
            return inv;
        }).when(invoiceRepository).save(isA(Invoice.class));

        mvc.perform(post("/createNewInvoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("InvoiceId").value(1))
                .andExpect(jsonPath("Cost").value(70.0));
//                .andDo(document("Add Invoice POST"
//                                , requestFields(
//                                        fieldWithPath("Company.Name").description("Company to which the invoice is created"),
//                                        fieldWithPath("Company.Id").description("Id of the Company to which the invoice is created"),
//                                        fieldWithPath("Company.Name").description("Name of the Company to which the invoice is created"),
//                                        fieldWithPath("Company.Address").description("Address of the Company to which the invoice is created"),
//                                        fieldWithPath("Company.Contact").description("Contact of the Company to which the invoice is created"),
//                                        fieldWithPath("Company.Contact.Id").description("Id of the Contact for thr company"),
//                                        fieldWithPath("Company.Contact.Name").description("Name of the Contact for thr company"),
//                                        fieldWithPath("Company.Contact.Title").description("Title of the Contact for thr company"),
//                                        fieldWithPath("Company.Contact.PhoneNumber").description("PhoneNumber of the Contact for thr company"),
//                                        fieldWithPath("InvoiceDate").description("Date the invoice is created"),
//                                        fieldWithPath("ModifiedDate").description("Date teh invoice is modified"),
//                                        fieldWithPath("Cost").description("Total cost of the invoice"),
//                                        fieldWithPath("InvoiceDetails").description("List of items in the invoice"),
//                                        fieldWithPath("InvoiceDetails.[0].Id").description("id Of the Detail list of Items "),
//                                        fieldWithPath("InvoiceDetails.[0].InvoiceId").description("invoiceId to which the Line Item details added"),
//                                        fieldWithPath("InvoiceDetails.[0].Items.Id").description("Internal ID of the added line item"),
//                                        fieldWithPath("InvoiceDetails.[0].Items.Description").description("Description of the line Item"),
//                                        fieldWithPath("InvoiceDetails.[0].Items.FeeType").description("Fee type is either Flat Fee or Rate based fee"),
//                                        fieldWithPath("InvoiceDetails.[0].Items.Quantity").description("Line item quantity"),
//                                        fieldWithPath("InvoiceDetails.[0].Items.Fee").description("Line item fee. This is Flat Fee/ Rate based fee"),
//                                        fieldWithPath("InvoiceDetails.[0].Items.Amount").description("This is line item amount/price"),
//                                        fieldWithPath("InvoiceDetails.[0].TotalPrice").description("Total price of the line item"),
//                                        fieldWithPath("Status").description("Payment status of the invoice")),
//
//                        responseFields(
//                                fieldWithPath("Company").description("Company to which the invoice is created"),
//                                fieldWithPath("Company.Id").description("Id of the Company to which the invoice is created"),
//                                fieldWithPath("Company.Name").description("Name of the Company to which the invoice is created"),
//                                fieldWithPath("Company.Address").description("Address of the Company to which the invoice is created"),
//                                fieldWithPath("Company.Contact").description("Contact of the Company to which the invoice is created"),
//                                fieldWithPath("Company.Contact.Id").description("Id of the Contact for thr company"),
//                                fieldWithPath("Company.Contact.Name").description("Name of the Contact for thr company"),
//                                fieldWithPath("Company.Contact.Title").description("Title of the Contact for thr company"),
//                                fieldWithPath("Company.Contact.PhoneNumber").description("PhoneNumber of the Contact for thr company"),
//                                fieldWithPath("InvoiceDate").description("Date the invoice is created"),
//                                fieldWithPath("ModifiedDate").description("Date teh invoice is modified"),
//                                fieldWithPath("Cost").description("Total cost of the invoice"),
//                               fieldWithPath("Status").description("Payment status of the invoice")
//                        )));
    }

    @Test
    void addNewLineItemTestWithFlatFee() throws Exception {
        List<Items> itemsList = new ArrayList<>();
        itemsList.add (new Items("item1",'F',0,0.0,20.0));
        itemsList.add (new Items("item2",'F',0,0.0,20.0));

        InvoiceDetails invoiceDetails = new InvoiceDetails(1, itemsList.get(0),itemsList.get(0).getAmount());
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice invoice=new Invoice(company, LocalDate.of(2021,07,12)
                ,"Unpaid",LocalDate.of (2021,07,12) ,0.0, null );


        Map<String, Object> requestBody= new HashMap<>();
        requestBody.put("InvoiceId", invoiceDetails.getInvoiceId());
        requestBody.put("Items", invoiceDetails.getLineItem());
        requestBody.put("TotalPrice", invoiceDetails.getTotalPrice());

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
                .andExpect(jsonPath("TotalPrice").value(20))
                .andDo(document("Add Invoice Item  with Flat Fee POST"
                        , requestFields(
                                        fieldWithPath("InvoiceId").description("invoiceId to which the Line Item details added"),
                                        fieldWithPath("Items.Id").description("Internal ID of the added line item"),
                                        fieldWithPath("Items.Description").description("Description of the line Item"),
                                        fieldWithPath("Items.FeeType").description("Fee type is either Flat Fee or Rate based fee"),
                                        fieldWithPath("Items.Quantity").description("Line item quantity"),
                                        fieldWithPath("Items.Fee").description("Line item fee. This is Flat Fee/ Rate based fee"),
                                        fieldWithPath("Items.Amount").description("This is line item amount/price"),
                                        fieldWithPath("TotalPrice").description("Total price of the line item"))
                        , responseFields(
                                fieldWithPath("Id").description("id Of the Detail list of Items "),
                                fieldWithPath("InvoiceId").description("invoiceId to which the Line Item details added"),
                                fieldWithPath("Items.Id").description("Internal ID of the added line item"),
                                fieldWithPath("Items.Description").description("Description of the line Item"),
                                fieldWithPath("Items.FeeType").description("Fee type is either Flat Fee or Rate based fee"),
                                fieldWithPath("Items.Quantity").description("Line item quantity"),
                                fieldWithPath("Items.Fee").description("Line item fee. This is Flat Fee/ Rate based fee"),
                                fieldWithPath("Items.Amount").description("This is line item amount/price"),
                                fieldWithPath("TotalPrice").description("Total price of the line item"))
                        )
                );
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
        requestBody.put("InvoiceId", invoiceDetails.getInvoiceId());
        requestBody.put("Items", invoiceDetails.getLineItem());
        requestBody.put("TotalPrice", invoiceDetails.getTotalPrice());

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
                .andExpect(jsonPath("TotalPrice").value(50))
                .andDo(document("Add Invoice Item with Rate POST"
                       , requestFields(
                                        fieldWithPath("InvoiceId").description("invoiceId to which the Line Item details added"),
                                        fieldWithPath("Items.Id").description("Internal ID of the added line item"),
                                        fieldWithPath("Items.Description").description("Description of the line Item"),
                                        fieldWithPath("Items.FeeType").description("Fee type is either Flat Fee or Rate based fee"),
                                        fieldWithPath("Items.Quantity").description("Line item quantity"),
                                        fieldWithPath("Items.Fee").description("Line item fee. This is Flat Fee/ Rate based fee"),
                                        fieldWithPath("Items.Amount").description("This is line item amount/price"),
                                        fieldWithPath("TotalPrice").description("Total price of the line item"))
                        , responseFields(
                                fieldWithPath("Id").description("id Of the Detail list of Items "),
                                fieldWithPath("InvoiceId").description("invoiceId to which the Line Item details added"),
                                fieldWithPath("Items.Id").description("Internal ID of the added line item"),
                                fieldWithPath("Items.Description").description("Description of the line Item"),
                                fieldWithPath("Items.FeeType").description("Fee type is either Flat Fee or Rate based fee"),
                                fieldWithPath("Items.Quantity").description("Line item quantity"),
                                fieldWithPath("Items.Fee").description("Line item fee. This is Flat Fee/ Rate based fee"),
                                fieldWithPath("Items.Amount").description("This is line item amount/price"),
                                fieldWithPath("TotalPrice").description("Total price of the line item"))
                ));
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

                .andExpect(jsonPath("InvoiceId").value(1))
                .andExpect(jsonPath("Status").value("Unpaid"))
                .andExpect(jsonPath("$.Company.Name").value("ABC..inc"))
                .andExpect(jsonPath("Cost").value(1.0));

    }

    @Test
    void modifyInvoiceTest() throws Exception{
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        List<Items> itemsList = new ArrayList<>();
        itemsList.add (new Items(1,"item1",'R',5,10.0,0.0));
        itemsList.add (new Items(2,"item2",'F',0,0.0,20.0));
        List<InvoiceDetails> invoiceDetailsList = new ArrayList<>();
        invoiceDetailsList.add(new InvoiceDetails(1,1, itemsList.get(0),itemsList.get(0).getQuantity() * itemsList.get(0).getFee()));
        invoiceDetailsList.add(new InvoiceDetails(2,1, itemsList.get(1),itemsList.get(1).getAmount()));
        Invoice existingInvoice=new Invoice(1,company, LocalDate.of(2021,07,12)
                ,"Unpaid",LocalDate.now() ,1.0, null );
        Contact contact1 = new Contact("Person3","Sales Rep","111-222-3333");
        Company company1=new Company("XYZ .inc","678 Street, New York,NY", contact1);

        Invoice changeDate=new Invoice (company, LocalDate.of(2021,07,16)
                ,"Unpaid",LocalDate.now() ,70.0, invoiceDetailsList);
        Invoice changeStatus=new Invoice (company, LocalDate.of(2021,07,12)
                ,"Paid",LocalDate.now() ,70.0, invoiceDetailsList);
        Invoice changeDateAndStatus=new Invoice (company, LocalDate.of(2021,07,16)
                ,"Paid",LocalDate.now() ,70.0, invoiceDetailsList);
        Invoice changeCompany=new Invoice (company1, LocalDate.of(2021,07,12)
                ,"Unpaid",LocalDate.now() ,70.0, invoiceDetailsList);
        String CurrentDate= LocalDate.now().toString();
        changeDate.setInvoiceId(1);
        changeStatus.setInvoiceId(1);
        changeDateAndStatus.setInvoiceId(1);
        changeCompany.setInvoiceId(1);

        doNothing().when(invoiceRepository).updateInvoiceDate(isA(LocalDate.class),isA(LocalDate.class),isA(Long.class));
        doNothing().when(invoiceRepository).updateInvoiceStatus(isA(String.class),isA(LocalDate.class),isA(Long.class));
        doNothing().when(invoiceRepository).updateInvoiceDateAndStatus(isA(String.class),isA(LocalDate.class),isA(LocalDate.class),isA(Long.class));
        doNothing().when(invoiceRepository).updateCompany(isA(Company.class),isA(LocalDate.class),isA(Long.class));

        when(invoiceRepository.findById(isA(Long.class)))
                .thenReturn(Optional.of(existingInvoice),Optional.of(changeDate))
               .thenReturn(Optional.of(existingInvoice),Optional.of(changeStatus))
                .thenReturn(Optional.of(existingInvoice),Optional.of(changeDateAndStatus))
                .thenReturn(Optional.of(existingInvoice),Optional.of(changeCompany))
               .thenReturn(Optional.of(existingInvoice),Optional.of(existingInvoice))
                .thenReturn(Optional.ofNullable(null));


       Map<String, Object> requestBody= new HashMap<>();
        requestBody.put("InvoiceDate",LocalDate.of(2021,07,16) );

        mvc.perform(patch("/invoice/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("InvoiceId").value(1))
                .andExpect(jsonPath("InvoiceDate").value("2021-07-16"))
                .andExpect(jsonPath("ModifiedDate").value(CurrentDate));

        Map<String, Object> requestBody1= new HashMap<>();
        requestBody1.put("Status","Paid" );
        mvc.perform(patch("/invoice/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("InvoiceId").value(1))
                .andExpect(jsonPath("Status").value("Paid"))
                .andExpect(jsonPath("ModifiedDate").value(CurrentDate));

        Map<String, Object> requestBody2= new HashMap<>();
        requestBody.put("InvoiceDate",LocalDate.of(2021,07,16) );
        requestBody1.put("Status","paid" );
        mvc.perform(patch("/invoice/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("InvoiceId").value(1))
                .andExpect(jsonPath("Status").value("Paid"))
                .andExpect(jsonPath("InvoiceDate").value("2021-07-16"))
                .andExpect(jsonPath("ModifiedDate").value(CurrentDate));

        Map<String, Object> requestBody3= new HashMap<>();
        requestBody.put("Company",company1);
        mvc.perform(patch("/invoice/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody3)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("InvoiceId").value(1))
                .andExpect(jsonPath("Company").value(company1))
                .andExpect(jsonPath("ModifiedDate").value(CurrentDate));

        Map<String, Object> requestBody4= new HashMap<>();

        mvc.perform(patch("/invoice/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody4)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("InvoiceId").value(1))
                .andExpect(jsonPath("Status").value("Unpaid"))
                .andExpect(jsonPath("InvoiceDate").value("2021-07-12"));

        mvc.perform(patch("/invoice/12")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isNotFound());

//                .andExpect(jsonPath("invoiceId").value(1))
//                .andExpect(jsonPath("status").value("Unpaid"))
//                .andExpect(jsonPath("$.company.Name").value("ABC..inc"))
//                .andExpect(jsonPath("cost").value(1.0));
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
                    .andExpect(jsonPath("$.content[0].createDate").value(dtoInvoiceDetails.get(0).getCreateDate().toString()))
                    .andExpect(jsonPath("$.content[0].total").value(dtoInvoiceDetails.get(0).getTotal()))
                    .andExpect(jsonPath("$.content[0].paidStatus").value(dtoInvoiceDetails.get(0).getPaidStatus()))
                    .andExpect(jsonPath("$.content[0].itemsDetails[0].Items.Description").value("item1"))
                    .andExpect(jsonPath("$.content[0].itemsDetails[0].Items.FeeType").value("F"))
                    .andExpect(jsonPath("$.content[0].itemsDetails[0].Items.Quantity").value(0))
                    .andExpect(jsonPath("content.length()").value(listOfInvoices.size()));

    }
    @Test
    void deleteInvoiceByInvoiceIdTest() throws Exception {
        Contact contact = new Contact("Person1", "Sales Rep", "111-222-3333");
        Company company = new Company("ABC..inc", "123 Street, Phoenix,AZ", contact);
        Invoice invoice = new Invoice(1, company, LocalDate.of(2020, 07, 12)
                , "Paid", LocalDate.of(2021, 07, 12), 1.0, null);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        doNothing().when(invoiceRepository).deleteById(1L);

        mvc.perform(delete("/deleteByInvoiceId/1"))
                .andExpect(status().isOk());

    }
    @Test
    void deleteInvoiceByInvoiceIdUnpaidTest() throws Exception {
        Contact contact = new Contact("Person1", "Sales Rep", "111-222-3333");
        Company company = new Company("ABC..inc", "123 Street, Phoenix,AZ", contact);
        Invoice invoice = new Invoice(1, company, LocalDate.of(2020, 07, 12)
                , "Unpaid", LocalDate.of(2021, 07, 12), 1.0, null);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        doNothing().when(invoiceRepository).deleteById(1L);

        mvc.perform(delete("/deleteByInvoiceId/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteInvoiceByInvoiceIdNotGreaterThanOneYearTest() throws Exception {
        Contact contact = new Contact("Person1", "Sales Rep", "111-222-3333");
        Company company = new Company("ABC..inc", "123 Street, Phoenix,AZ", contact);
        Invoice invoice = new Invoice(1, company, LocalDate.of(2021, 07, 12)
                , "Paid", LocalDate.of(2021, 07, 12), 1.0, null);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        doNothing().when(invoiceRepository).deleteById(1L);

        mvc.perform(delete("/deleteByInvoiceId/1"))
                .andExpect(status().isNotFound());
    }
    @Test
    void deleteInvoiceByInvoiceIdNotExist() throws Exception {
        Contact contact = new Contact("Person1", "Sales Rep", "111-222-3333");
        Company company = new Company("ABC..inc", "123 Street, Phoenix,AZ", contact);
        Invoice invoice = new Invoice(1, company, LocalDate.of(2021, 07, 12)
                , "Paid", LocalDate.of(2021, 07, 12), 1.0, null);

        when(invoiceRepository.findById(2L)).thenReturn(Optional.of(invoice));
        doNothing().when(invoiceRepository).deleteById(2L);

        mvc.perform(delete("/deleteByInvoiceId/2"))
                .andExpect(status().isNotFound());
    }

}

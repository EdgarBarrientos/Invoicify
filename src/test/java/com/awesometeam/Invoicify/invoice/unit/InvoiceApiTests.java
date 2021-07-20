package com.awesometeam.Invoicify.invoice.unit;

import com.awesometeam.Invoicify.company.model.Company;
import com.awesometeam.Invoicify.company.model.Contact;
import com.awesometeam.Invoicify.invoice.controller.InvoiceController;
import com.awesometeam.Invoicify.invoice.model.Invoice;
import com.awesometeam.Invoicify.invoice.model.InvoiceDetails;
import com.awesometeam.Invoicify.invoice.model.Items;
import com.awesometeam.Invoicify.invoice.service.InvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(InvoiceController.class)
public class InvoiceApiTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    InvoiceService invoiceservice;

    @Test
    void createNewInvoiceTest() throws  Exception{
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
        requestBody.put("ModifiedDate", invoice.getInvoiceDate());
        requestBody.put("Cost", invoice.getCost());
        requestBody.put("InvoiceDetails", invoiceDetailsList);

        doAnswer(invocation ->{
            Invoice inv=invocation.getArgument(0);
            inv.setInvoiceId(1);
            return inv;
        }).when(invoiceservice).createNewInvoices(isA(Invoice.class));

        mvc.perform(post("/createNewInvoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("InvoiceId").value(1))
                .andExpect(jsonPath("Cost").value(70.0));
    }

    @Test
    void addNewLineItemsTestWithFlatFee() throws Exception
    {
        List<Items> itemsList = new ArrayList<>();
        itemsList.add (new Items(1,"item1",'F',0,0.0,20.0));
        itemsList.add (new Items(2,"item2",'F',0,0.0,20.0));

        InvoiceDetails invoiceDetails = new InvoiceDetails(1, itemsList.get(0),itemsList.get(0).getAmount());

        Map<String, Object> requestBody= new HashMap<>();
        requestBody.put("InvoiceId", invoiceDetails.getInvoiceId());
        requestBody.put("Items", invoiceDetails.getLineItem());
        requestBody.put("TotalPrice", invoiceDetails.getTotalPrice());


        doAnswer(invocation -> {
            InvoiceDetails invoiceDetails1 = invocation.getArgument(0);

            invoiceDetails1.setId(1);

            invoiceDetails1.setInvoiceId(1L);

            return invoiceDetails1;
        }).when(invoiceservice).addNewLineItem(isA(InvoiceDetails.class));

        this.mvc.perform(post("/addInvoiceItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated());
    }

     @Test
    void addNewLineItemsTestWithRate() throws Exception
    {
        List<Items> itemsList = new ArrayList<>();
        itemsList.add (new Items(1,"item1",'R',5,10.0,0.0));
        itemsList.add (new Items(2,"item2",'R',5,20.0,0.0));

        InvoiceDetails invoiceDetails = new InvoiceDetails(1, itemsList.get(0),itemsList.get(0).getQuantity() * itemsList.get(0).getFee());

        Map<String, Object> requestBody= new HashMap<>();
        requestBody.put("InvoiceId", invoiceDetails.getInvoiceId());
        requestBody.put("Items", invoiceDetails.getLineItem());
        requestBody.put("TotalPrice", invoiceDetails.getTotalPrice());

        doAnswer(invocation -> {
            InvoiceDetails invoiceDetails1 = invocation.getArgument(0);

            invoiceDetails1.setId(1);

            invoiceDetails1.setInvoiceId(1L);

            return invoiceDetails1;
        }).when(invoiceservice).addNewLineItem(isA(InvoiceDetails.class));

        this.mvc.perform(post("/addInvoiceItem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated());
    }

    @Test
    void findInvoiceByInvoiceIdTest() throws Exception{
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice invoice=new Invoice(1,company, LocalDate.of(2021,07,12)
                ,"Unpaid",LocalDate.of (2021,07,12) ,1.0, null );

        when(invoiceservice.findByInvoiceId(1)).thenReturn(invoice);

        mvc.perform(get("/invoice/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("InvoiceId").value(1))
                .andExpect(jsonPath("Status").value("Unpaid"))
                .andExpect(jsonPath("$.Company.Name").value("ABC..inc"))
                .andExpect(jsonPath("Cost").value(1.0));

    }

    @Test
    void modifyInvoiceWithUnpaidStatusTest() throws Exception{
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
        invoice.setInvoiceId(1);

        doAnswer(invocation ->{
            Invoice inv=invocation.getArgument(0);
            Long index= invocation.getArgument(1);
            Invoice result=null;
            if(index == 1){
                result = new Invoice(invoice.getCompany(),invoice.getInvoiceDate(),
                        invoice.getStatus(),LocalDate.now(),invoice.getCost(),
                        invoice.getInvoiceDetailsList());
                result.setInvoiceId(1);
               if(result.getStatus() !="Paid") {
                   if (inv.getInvoiceDate() != null) {
                       result.setInvoiceDate(inv.getInvoiceDate());
                   }
                   if (inv.getStatus() != null) {
                       result.setStatus(inv.getStatus());
                   }
                   if (inv.getStatus() != null && inv.getInvoiceDate() != null) {
                       result.setStatus(inv.getStatus());
                       result.setInvoiceDate(inv.getInvoiceDate());
                   }
                   if (inv.getCompany() != null) {
                       result.setCompany(inv.getCompany());
                   }
               }
            }
            return Optional.ofNullable(result);

        }).when(invoiceservice).modifyInvoice(isA(Invoice.class),isA(long.class));

        String CurrentDate= LocalDate.now().toString();
        Map<String, Object> requestBody= new HashMap<>();
        requestBody.put("InvoiceDate",LocalDate.of(2021,07,17) );

        mvc.perform(patch("/invoice/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("InvoiceId").value(1))
                .andExpect(jsonPath("InvoiceDate").value("2021-07-17"))
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
        requestBody2.put("Status","Paid" );
        requestBody2.put("InvoiceDate",LocalDate.of(2021,07,17) );
        mvc.perform(patch("/invoice/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("InvoiceId").value(1))
                .andExpect(jsonPath("Status").value("Paid"))
                .andExpect(jsonPath("InvoiceDate").value("2021-07-17"))
                .andExpect(jsonPath("ModifiedDate").value(CurrentDate));

        Map<String, Object> requestBody3= new HashMap<>();
        Contact contact1 = new Contact("Person3","Sales Rep","111-222-3333");
        Company company1=new Company("XYZ .inc","678 Street, New York,NY", contact1);
        requestBody3.put("Company",company1 );
        requestBody3.put("InvoiceDate",LocalDate.of(2021,07,17) );
        mvc.perform(patch("/invoice/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody3)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("InvoiceId").value(1))
                .andExpect(jsonPath("Company").value(company1))
                .andExpect(jsonPath("ModifiedDate").value(CurrentDate));
    }

    @Test
    void modifyInvoiceWithPaidStatusTest() throws Exception{
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        List<Items> itemsList = new ArrayList<>();
        itemsList.add (new Items(1,"item1",'R',5,10.0,0.0));
        itemsList.add (new Items(2,"item2",'F',0,0.0,20.0));
        List<InvoiceDetails> invoiceDetailsList = new ArrayList<>();
        invoiceDetailsList.add(new InvoiceDetails(1, itemsList.get(0),itemsList.get(0).getQuantity() * itemsList.get(0).getFee()));
        invoiceDetailsList.add(new InvoiceDetails(1, itemsList.get(1),itemsList.get(1).getAmount()));

        Invoice invoice=new Invoice(company, LocalDate.of(2021,07,12)
                ,"Paid",null,70.0, invoiceDetailsList );
        invoice.setInvoiceId(1);

        doAnswer(invocation ->{
            Invoice inv=invocation.getArgument(0);
            Long index= invocation.getArgument(1);
            Invoice result=null;
            if(index == 1){
                result = new Invoice(invoice.getCompany(),invoice.getInvoiceDate(),
                        invoice.getStatus(),null,invoice.getCost(),
                        invoice.getInvoiceDetailsList());
                result.setInvoiceId(1);
                if(result.getStatus() !="Paid") {
                    if (inv.getInvoiceDate() != null) {
                        result.setInvoiceDate(inv.getInvoiceDate());
                    }
                    if (inv.getStatus() != null) {
                        result.setStatus(inv.getStatus());
                    }
                    if (inv.getStatus() != null && inv.getInvoiceDate() != null) {
                        result.setStatus(inv.getStatus());
                        result.setInvoiceDate(inv.getInvoiceDate());
                    }
                    if (inv.getCompany() != null) {
                        result.setCompany(inv.getCompany());
                    }
                }
            }
            return Optional.ofNullable(result);

        }).when(invoiceservice).modifyInvoice(isA(Invoice.class),isA(long.class));

        Map<String, Object> requestBody3= new HashMap<>();
        Contact contact1 = new Contact("Person3","Sales Rep","111-222-3333");
        Company company1=new Company("XYZ .inc","678 Street, New York,NY", contact1);
        requestBody3.put("Company",company1 );
        requestBody3.put("InvoiceDate",LocalDate.of(2021,07,17) );
        mvc.perform(patch("/invoice/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody3)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("InvoiceId").value(1))
                .andExpect(jsonPath("Company").value(company))
                .andExpect(jsonPath("ModifiedDate").doesNotExist());
    }

}

package com.awesometeam.Invoicify.invoice.unit;

import com.awesometeam.Invoicify.company.model.Company;
import com.awesometeam.Invoicify.company.model.Contact;
import com.awesometeam.Invoicify.invoice.controller.InvoiceController;
import com.awesometeam.Invoicify.invoice.dto.DtoInvoiceDetails;
import com.awesometeam.Invoicify.invoice.model.Invoice;
import com.awesometeam.Invoicify.invoice.model.InvoiceDetails;
import com.awesometeam.Invoicify.invoice.model.Items;
import com.awesometeam.Invoicify.invoice.service.InvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        requestBody.put("company", invoice.getCompany());
        requestBody.put("invoiceDate", invoice.getInvoiceDate());
        requestBody.put("status", invoice.getStatus());
        requestBody.put("modifiedDate", invoice.getInvoiceDate());
        requestBody.put("cost", invoice.getCost());
        requestBody.put("invoiceDetails", invoiceDetailsList);

        doAnswer(invocation ->{
            Invoice inv=invocation.getArgument(0);
            inv.setInvoiceId(1);
            return inv;
        }).when(invoiceservice).createNewInvoices(isA(Invoice.class));

        mvc.perform(post("/createNewInvoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("invoiceId").value(1))
                .andExpect(jsonPath("cost").value(70.0));
    }

    @Test
    void addNewLineItemsTestWithFlatFee() throws Exception
    {
        List<Items> itemsList = new ArrayList<>();
        itemsList.add (new Items(1,"item1",'F',0,0.0,20.0));
        itemsList.add (new Items(2,"item2",'F',0,0.0,20.0));

        InvoiceDetails invoiceDetails = new InvoiceDetails(1, itemsList.get(0),itemsList.get(0).getAmount());

        Map<String, Object> requestBody= new HashMap<>();
        requestBody.put("invoiceId", invoiceDetails.getInvoiceId());
        requestBody.put("lineItem", invoiceDetails.getLineItem());
        requestBody.put("totalPrice", invoiceDetails.getTotalPrice());


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
        requestBody.put("invoiceId", invoiceDetails.getInvoiceId());
        requestBody.put("lineItem", invoiceDetails.getLineItem());
        requestBody.put("totalPrice", invoiceDetails.getTotalPrice());

        doAnswer(invocation -> {
            InvoiceDetails invoiceDetails1 = invocation.getArgument(0);

            invoiceDetails1.setId(1);

            invoiceDetails1.setInvoiceId(1L);

            return invoiceDetails1;
        }).when(invoiceservice).addNewLineItem(isA(InvoiceDetails.class));

        //when(service.addNewLineItem(invoiceDetails)).thenReturn(invoiceDetails);
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
                .andExpect(jsonPath("invoiceId").value(1))
                .andExpect(jsonPath("status").value("Unpaid"))
                .andExpect(jsonPath("$.company.Name").value("ABC..inc"))
                .andExpect(jsonPath("cost").value(1.0));

    }

    @Test
    void findUnpaidInvoiceByCompany() throws Exception{

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
        when(invoiceservice.findByCompanyIdAndStatus(1L, "Unpaid", 0, 10)).thenReturn(page);

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

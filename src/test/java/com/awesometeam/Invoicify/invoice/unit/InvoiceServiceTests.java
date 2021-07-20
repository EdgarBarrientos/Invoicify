package com.awesometeam.Invoicify.invoice.unit;

import com.awesometeam.Invoicify.company.model.Company;
import com.awesometeam.Invoicify.company.model.Contact;
import com.awesometeam.Invoicify.invoice.model.Invoice;
import com.awesometeam.Invoicify.invoice.model.InvoiceDetails;
import com.awesometeam.Invoicify.invoice.model.Items;
import com.awesometeam.Invoicify.invoice.repository.InvoiceDetailsRepository;
import com.awesometeam.Invoicify.invoice.repository.InvoiceRepository;
import com.awesometeam.Invoicify.invoice.repository.ItemsRepository;
import com.awesometeam.Invoicify.invoice.service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTests {
    @Mock
    InvoiceRepository invoiceRepository;

    @Mock
    InvoiceDetailsRepository invoiceDetailsRepository;

    @Mock
    ItemsRepository itemsRepository;

    @InjectMocks
    InvoiceService invoiceService;

    @Test
    void addNewInvoiceWithNoItems() throws  Exception{
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        List<Items> itemsList = new ArrayList<>();
        itemsList.add (new Items(1,"item1",'R',5,10.0,0.0));
        itemsList.add (new Items(2,"item2",'F',0,0.0,20.0));

        Invoice invoice=new Invoice(company, LocalDate.of(2021,07,12)
                ,"UnPaid",LocalDate.of (2021,07,12) ,0.0, null );

        Map<String, Object> requestBody= new HashMap<>();

        requestBody.put("company", invoice.getCompany());
        requestBody.put("invoiceDate", invoice.getInvoiceDate());
        requestBody.put("status", invoice.getStatus());
        requestBody.put("modifiedDate", invoice.getInvoiceDate());
        requestBody.put("cost", invoice.getCost());
        requestBody.put("invoiceDetails", null);

        when(invoiceRepository.save(invoice)).thenReturn(invoice);

        Invoice expected =invoiceService.createNewInvoices(invoice);
        assertEquals(expected,invoice);
    }

    @Test
    void addNewInvoiceWithItems() throws  Exception{
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        List<Items> itemsList = new ArrayList<>();
        itemsList.add (new Items(1,"item1",'R',5,10.0,0.0));
        itemsList.add (new Items(2,"item2",'F',0,0.0,20.0));
        List<InvoiceDetails> invoiceDetailsList = new ArrayList<>();
        invoiceDetailsList.add(new InvoiceDetails(1, itemsList.get(0),itemsList.get(0).getQuantity() * itemsList.get(0).getFee()));
        invoiceDetailsList.add(new InvoiceDetails(1, itemsList.get(1),itemsList.get(1).getAmount()));
        Invoice invoice=new Invoice(company, LocalDate.of(2021,07,12)
                ,"UnPaid",LocalDate.of (2021,07,12) ,0.0, invoiceDetailsList );

        Map<String, Object> requestBody= new HashMap<>();

        requestBody.put("company", invoice.getCompany());
        requestBody.put("invoiceDate", invoice.getInvoiceDate());
        requestBody.put("status", invoice.getStatus());
        requestBody.put("modifiedDate", invoice.getInvoiceDate());
        requestBody.put("cost", invoice.getCost());
        requestBody.put("invoiceDetails", invoiceDetailsList);

        when(invoiceRepository.save(invoice)).thenReturn(invoice);
        when(invoiceDetailsRepository.save(invoiceDetailsList.get(0))).thenReturn(invoiceDetailsList.get(0));
        when(invoiceDetailsRepository.save(invoiceDetailsList.get(1))).thenReturn(invoiceDetailsList.get(1));

        Invoice expected =invoiceService.createNewInvoices(invoice);
        assertEquals(expected,invoice);
    }

    @Test
    void addNewLineItemsTestWithFlatFee() {
        List<Items> itemsList = new ArrayList<>();
        itemsList.add (new Items(1,"item1",'F',0,0.0,20.0));
        itemsList.add (new Items(2,"item2",'F',0,0.0,20.0));

        InvoiceDetails invoiceDetails = new InvoiceDetails(1, itemsList.get(0),20.0);

        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice invoice=new Invoice(1,company, LocalDate.of(2021,07,12)
                ,"UnPaid",LocalDate.of (2021,07,12) ,0.0, null );
        when(invoiceDetailsRepository.save(invoiceDetails)).thenReturn(invoiceDetails);
        InvoiceDetails actual = invoiceService.addNewLineItem(invoiceDetails);
        assertEquals(invoiceDetails, actual);
    }

    @Test
    void addNewLineItemsTestWithRate() {
        List<Items> itemsList = new ArrayList<>();
        itemsList.add (new Items("item1",'R',5,10.0,0.0));
        itemsList.add (new Items("item2",'R',5,20.0,0.0));

        InvoiceDetails invoiceDetails = new InvoiceDetails(1, itemsList.get(0),itemsList.get(0).getQuantity() * itemsList.get(0).getFee());
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice invoice=new Invoice(1,company, LocalDate.of(2021,07,12)
                ,"UnPaid",LocalDate.of (2021,07,12) ,0.0, null );
        when(invoiceDetailsRepository.save(invoiceDetails)).thenReturn(invoiceDetails);
        InvoiceDetails actual = invoiceService.addNewLineItem(invoiceDetails);
        assertEquals(invoiceDetails, actual);
    }

    @Test
    void findInvoiceByInvoiceIdTest() throws Exception {
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice invoice=new Invoice(1,company, LocalDate.of(2021,07,12)
                ,"UnPaid",LocalDate.of (2021,07,12) ,1.0, null );

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        Invoice actual =invoiceService.findByInvoiceId(1);

        assertEquals(actual,invoice);

    }

    @Test
    void findInvoiceByInvoiceIdReturnsNotFoundTest() throws Exception {
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice invoice=new Invoice(1,company, LocalDate.of(2021,07,12)
                ,"UnPaid",LocalDate.of (2021,07,12) ,1.0, null );

        when(invoiceRepository.findById(2L)).thenReturn(Optional.of(invoice));

        Invoice actual =invoiceService.findByInvoiceId(2);

        assertEquals(actual,invoice);


    }

    @Test
    void modifyInvoiceDateTest()
    {
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice existingInvoice=new Invoice(1,company, LocalDate.of(2021,07,12)
                ,"Unpaid",LocalDate.now() ,1.0, null );
        Invoice invoice=new Invoice(1,company, LocalDate.of(2021,07,12)
                ,"UnPaid",LocalDate.now() ,1.0, null );
        Invoice invoice1=new Invoice(null, LocalDate.of(2021,12,12)
                ,null,null ,0.0, null );

        when(invoiceRepository.findById(isA(Long.class)))
                .thenReturn(Optional.ofNullable(existingInvoice),Optional.of(invoice));
        Invoice result = invoiceService.modifyInvoice(invoice1,1).orElse(null);
        assertEquals(invoice,result);
    }

    @Test
    void modifyInvoiceStatusTest()
    {
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice existingInvoice=new Invoice(1,company, LocalDate.of(2021,07,12)
                ,"Unpaid",LocalDate.now() ,1.0, null );
        Invoice invoice=new Invoice(1,company, LocalDate.of(2021,07,12)
                ,"Paid",LocalDate.now()  ,1.0, null );
        Invoice invoice1=new Invoice(null, null
                ,"Paid",null ,0.0, null );

        when(invoiceRepository.findById(isA(Long.class)))
                .thenReturn(Optional.of(existingInvoice),Optional.ofNullable(invoice));
        Invoice result = invoiceService.modifyInvoice(invoice1,1).orElse(null);

    }

    @Test
    void modifyInvoiceDateAndStatusTest()
    {
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice existingInvoice=new Invoice(1,company, LocalDate.of(2021,07,12)
                ,"Unpaid",LocalDate.now() ,1.0, null );
        Invoice invoice=new Invoice(1,company, LocalDate.of(2021,07,12)
                ,"Paid",LocalDate.now() ,1.0, null );
        Invoice invoice1=new Invoice(null, LocalDate.of(2021,12,12)
                ,"Paid",null ,0.0, null );

        when(invoiceRepository.findById(isA(Long.class)))
                .thenReturn(Optional.of(existingInvoice),Optional.of(invoice));

        Invoice result = invoiceService.modifyInvoice(invoice1,1).orElse(null);
        assertEquals(invoice,result);

    }

    @Test
    void modifyInvoiceCompanyTest()
    {
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Contact contact1 = new Contact("Person3","Sales Rep","111-222-3333");
        Company company1=new Company("XYZ .inc","678 Street, New York,NY", contact1);
        Invoice existingInvoice=new Invoice(1,company, LocalDate.of(2021,07,12)
                ,"Unpaid",LocalDate.now() ,1.0, null );
        Invoice invoice=new Invoice(1,company1, LocalDate.of(2021,07,12)
                ,"Unpaid",LocalDate.now() ,1.0, null );
        Invoice invoice1=new Invoice( company1,null,null,null ,0.0, null );


        when(invoiceRepository.findById(isA(Long.class)))
                .thenReturn(Optional.of(existingInvoice),Optional.of(invoice));

        Invoice result = invoiceService.modifyInvoice(invoice1,1).orElse(null);
        assertEquals(invoice,result);

    }

    @Test
    void modifyInvoiceAlreadyInPaidStatusTest()
    {
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Contact contact1 = new Contact("Person3","Sales Rep","111-222-3333");
        Company company1=new Company("XYZ .inc","678 Street, New York,NY", contact1);
        Invoice invoice=new Invoice(1,company1, LocalDate.of(2021,07,12)
                ,"Paid",LocalDate.now() ,1.0, null );
        Invoice invoice1=new Invoice( company1,null,null,null ,0.0, null );

        when(invoiceRepository.findById(isA(Long.class)))
                .thenReturn(Optional.of(invoice))
                .thenReturn(Optional.ofNullable(null));

        Invoice result = invoiceService.modifyInvoice(invoice1,1).orElse(null);
        assertNull(result);

        result = invoiceService.modifyInvoice(invoice1,12).orElse(null);
        assertNull(result);
    }
}

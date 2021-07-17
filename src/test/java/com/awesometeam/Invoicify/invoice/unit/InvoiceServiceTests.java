package com.awesometeam.Invoicify.invoice.unit;

import com.awesometeam.Invoicify.company.model.Company;
import com.awesometeam.Invoicify.company.model.Contact;
import com.awesometeam.Invoicify.invoice.dto.DtoInvoiceDetails;
import com.awesometeam.Invoicify.invoice.model.Invoice;
import com.awesometeam.Invoicify.invoice.model.InvoiceDetails;
import com.awesometeam.Invoicify.invoice.model.Items;
import com.awesometeam.Invoicify.invoice.repository.InvoiceDetailsRepository;
import com.awesometeam.Invoicify.invoice.repository.InvoiceRepository;
import com.awesometeam.Invoicify.invoice.repository.ItemsRepository;
import com.awesometeam.Invoicify.invoice.service.InvoiceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void addNewInvoice() throws  Exception{
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        List<Items> itemsList = new ArrayList<>();
        itemsList.add (new Items(1,"item1",'R',5,10.0,0.0));
        itemsList.add (new Items(2,"item2",'F',0,0.0,20.0));
        List<InvoiceDetails> invoiceDetailsList = new ArrayList<>();
        invoiceDetailsList.add(new InvoiceDetails(1,1, itemsList.get(0),itemsList.get(0).getQuantity() * itemsList.get(0).getFee()));
        invoiceDetailsList.add(new InvoiceDetails(2,1, itemsList.get(1),itemsList.get(1).getAmount()));
        Invoice invoice=new Invoice(company, LocalDate.of(2021,07,12)
                ,"Unpaid",LocalDate.of (2021,07,12) ,0.0, invoiceDetailsList );
        //System.out.println(invoice.toString());
        Map<String, Object> requestBody= new HashMap<>();

        requestBody.put("company", invoice.getCompany());
        requestBody.put("invoiceDate", invoice.getInvoiceDate());
        requestBody.put("status", invoice.getStatus());
        requestBody.put("modifiedDate", invoice.getInvoiceDate());
        requestBody.put("cost", invoice.getCost());
        requestBody.put("invoiceDetails", invoiceDetailsList);
        //System.out.println(requestBody.get("invoiceDetails").toString());

        when(invoiceRepository.save(invoice)).thenReturn(invoice);

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
                ,"Unpaid",LocalDate.of (2021,07,12) ,0.0, null );
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
                ,"Unpaid",LocalDate.of (2021,07,12) ,0.0, null );
        when(invoiceDetailsRepository.save(invoiceDetails)).thenReturn(invoiceDetails);
        InvoiceDetails actual = invoiceService.addNewLineItem(invoiceDetails);
        assertEquals(invoiceDetails, actual);
    }


    @Test
    void findInvoiceByInvoiceIdTest() throws Exception {
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice invoice=new Invoice(1,company, LocalDate.of(2021,07,12)
                ,"Unpaid",LocalDate.of (2021,07,12) ,1.0, null );

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        Invoice actual =invoiceService.findByInvoiceId(1);

        assertEquals(actual,invoice);

    }
    @Test
    void findInvoiceByInvoiceIdReturnsNotFoundTest() throws Exception {
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice invoice=new Invoice(1,company, LocalDate.of(2021,07,12)
                ,"Unpaid",LocalDate.of (2021,07,12) ,1.0, null );

        when(invoiceRepository.findById(2L)).thenReturn(Optional.of(invoice));

        Invoice actual =invoiceService.findByInvoiceId(2);

        assertEquals(actual,invoice);


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
        when(invoiceRepository.findByCompanyIdAndStatus(1L, "Unpaid", paging)).thenReturn(page);
        Page actual = invoiceService.findByCompanyIdAndStatus(1l, "Unpaid", 0, 10);
        assertEquals(listOfInvoices, actual.getContent());
    }

    @Test
    void deleteInvoiceByInvoiceIdTest() throws Exception {
        Contact contact = new Contact("Person1", "Sales Rep", "111-222-3333");
        Company company = new Company("ABC..inc", "123 Street, Phoenix,AZ", contact);
        Invoice invoice = new Invoice(1, company, LocalDate.of(2020, 07, 12)
                , "Paid", LocalDate.of(2021, 07, 12), 1.0, null);

        doNothing().when(invoiceRepository).deleteById(1L);
        Assertions.assertDoesNotThrow(()->invoiceService.deleteByInvoice(invoice) );

    }
    @Test
    void deleteInvoiceByInvoiceIdUnpaidTest() throws Exception {
        Contact contact = new Contact("Person1", "Sales Rep", "111-222-3333");
        Company company = new Company("ABC..inc", "123 Street, Phoenix,AZ", contact);
        Invoice invoice = new Invoice(1, company, LocalDate.of(2020, 07, 12)
                , "Unpaid", LocalDate.of(2021, 07, 12), 1.0, null);

        Assertions.assertThrows(RuntimeException.class, ()->invoiceService.deleteByInvoice(invoice));
    }
    @Test
    void deleteInvoiceByInvoiceIdNotGreaterThanOneYearTest() throws Exception {
        Contact contact = new Contact("Person1", "Sales Rep", "111-222-3333");
        Company company = new Company("ABC..inc", "123 Street, Phoenix,AZ", contact);
        Invoice invoice = new Invoice(1, company, LocalDate.of(2021, 07, 12)
                , "Paid", LocalDate.of(2021, 07, 12), 1.0, null);

        Assertions.assertThrows(RuntimeException.class, ()->invoiceService.deleteByInvoice(invoice));
    }

}

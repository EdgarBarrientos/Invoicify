package com.awesometeam.Invoicify.invoice.unit;

import com.awesometeam.Invoicify.company.model.Company;
import com.awesometeam.Invoicify.company.model.Contact;
import com.awesometeam.Invoicify.invoice.model.Invoice;
import com.awesometeam.Invoicify.invoice.repository.InvoiceRepository;
import com.awesometeam.Invoicify.invoice.service.InvoiceService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTests {
    @Mock
    InvoiceRepository invoiceRepository;

    @InjectMocks
    InvoiceService invoiceService;

    @Test
    void addNewInvoice() throws  Exception{
        Contact contact = new Contact( "Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice invoice=new Invoice(company, new Date(2021,07,12)
                ,"Unpaid",new Date (2021,07,12) ,0.0, null );
        Map<String, Object> requestBody= new HashMap<>();
        requestBody.put("company", invoice.getCompany());
        requestBody.put("invoiceDate", invoice.getInvoiceDate());
        requestBody.put("status", invoice.getStatus());
        requestBody.put("invoiceDate", invoice.getInvoiceDate());
        requestBody.put("cost", invoice.getCost());
        requestBody.put("invoiceDetails", null);

        when(invoiceRepository.save(invoice)).thenReturn(invoice);

        Invoice expected =invoiceService.createNewInvoices(invoice);
        assertEquals(expected,invoice);
    }

    @Test
    void findInvoiceByInvoiceIdTest() throws Exception {
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice invoice=new Invoice(1,company, new Date(2021,07,12)
                ,"Unpaid",new Date (2021,07,12) ,1.0, null );

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        Invoice actual =invoiceService.findByInvoiceId(1);

        assertEquals(actual,invoice);

    }
    @Test
    void findInvoiceByInvoiceIdReturnsNotFoundTest() throws Exception {
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice invoice=new Invoice(1,company, new Date(2021,07,12)
                ,"Unpaid",new Date (2021,07,12) ,1.0, null );

        when(invoiceRepository.findById(2L)).thenReturn(Optional.of(invoice));

        Invoice actual =invoiceService.findByInvoiceId(2);

        assertEquals(actual,invoice);


    }

    @Test
    void findUnpaidInvoiceByCompany() throws Exception{
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice invoice=new Invoice(1,company, new Date(2021,07,12)
                ,"Unpaid",new Date (2021,07,12) ,1.0, null );
        Invoice invoice2 =new Invoice(2,company, new Date(2021,07,11)
                ,"Unpaid",new Date (2021,07,12) ,1.0, null );
        List<Invoice> invoiceList = new ArrayList<>();
        invoiceList.add(invoice2);
        invoiceList.add(invoice);
        Pageable paging = PageRequest.of(0, 10, Sort.by("invoiceDate").ascending());
        Page page = new PageImpl(invoiceList, paging, 10);
        when(invoiceRepository.findByCompanyIdAndStatus(1L, "Unpaid", paging)).thenReturn(page);
        Page actual = invoiceService.findByCompanyIdAndStatus(1l, "Unpaid", 0, 10);
        assertEquals(invoiceList, actual.getContent());
    }
}

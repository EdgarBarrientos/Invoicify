package com.awesometeam.Invoicify.invoice.unit;

import com.awesometeam.Invoicify.company.model.Company;
import com.awesometeam.Invoicify.company.model.Contact;
import com.awesometeam.Invoicify.invoice.model.Invoice;
import com.awesometeam.Invoicify.invoice.model.InvoiceDetails;
import com.awesometeam.Invoicify.invoice.model.Items;
import com.awesometeam.Invoicify.invoice.repository.InvoiceDetailsRepository;
import com.awesometeam.Invoicify.invoice.repository.InvoiceRepository;
import com.awesometeam.Invoicify.invoice.service.InvoiceDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InvoiceDetailsServiceTests {

    @Mock
    InvoiceDetailsRepository invoiceDetailsRepository;
    @Mock
    InvoiceRepository invoiceRepository;
    @InjectMocks
    InvoiceDetailsService invoiceDetailsService;

//    @Test
//    void addNewLineItemsTestWithFlatFee() {
//        List<Items> itemsList = new ArrayList<>();
//        itemsList.add (new Items(1,"item1",'F',0,0.0,20.0));
//        itemsList.add (new Items(2,"item2",'F',0,0.0,20.0));
//
//        InvoiceDetails invoiceDetails = new InvoiceDetails(1, itemsList.get(0),20.0);
//
//        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
//        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
//        Invoice invoice=new Invoice(1,company, new Date(2021,07,12)
//                ,"Unpaid",new Date (2021,07,12) ,0.0, null );
//
//        when(invoiceDetailsRepository.save(invoiceDetails)).thenReturn(invoiceDetails);
//        when(invoiceRepository.findById(invoiceDetails.getInvoiceId())).thenReturn(Optional.of(invoice));
//        InvoiceDetails actual = invoiceDetailsService.addNewLineItem(invoiceDetails);
//        assertEquals(invoiceDetails, actual);
//    }
//
//    @Test
//    void addNewLineItemsTestWithRate() {
//        List<Items> itemsList = new ArrayList<>();
//        itemsList.add (new Items(1,"item1",'R',5,10.0,0.0));
//        itemsList.add (new Items(2,"item2",'R',5,20.0,0.0));
//
//        InvoiceDetails invoiceDetails = new InvoiceDetails(1, itemsList.get(0),itemsList.get(0).getQuantity() * itemsList.get(0).getFee());
//
//        when(invoiceDetailsRepository.save(invoiceDetails)).thenReturn(invoiceDetails);
//        InvoiceDetails actual = invoiceDetailsService.addNewLineItem(invoiceDetails);
//        assertEquals(invoiceDetails, actual);
//    }

}

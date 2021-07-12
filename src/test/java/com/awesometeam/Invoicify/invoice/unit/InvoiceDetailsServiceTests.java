package com.awesometeam.Invoicify.invoice.unit;

import com.awesometeam.Invoicify.invoice.model.InvoiceDetails;
import com.awesometeam.Invoicify.invoice.model.Items;
import com.awesometeam.Invoicify.invoice.repository.InvoiceDetailsRepository;
import com.awesometeam.Invoicify.invoice.service.InvoiceDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InvoiceDetailsServiceTests {

    @Mock
    InvoiceDetailsRepository invoiceDetailsRepository;

    @InjectMocks
    InvoiceDetailsService invoiceDetailsService;

    @Test
    void addNewLineItemsTest() {
        List<Items> itemsList = new ArrayList<>();
        itemsList.add (new Items(1L,"item1",'F',0,0.0,20.0));
        itemsList.add (new Items(2L,"item2",'R',10,5.0,0.0));
        InvoiceDetails invoiceDetails = new InvoiceDetails(itemsList.get(0),itemsList.get(0).getAmount());
        when(invoiceDetailsRepository.save(invoiceDetails)).thenReturn(invoiceDetails);
        InvoiceDetails actual = invoiceDetailsService.addNewLineItem(invoiceDetails);
        assertEquals(invoiceDetails, actual);
    }
}

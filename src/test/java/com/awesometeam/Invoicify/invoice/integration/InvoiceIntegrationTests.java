package com.awesometeam.Invoicify.invoice.integration;

import com.awesometeam.Invoicify.company.model.Company;
import com.awesometeam.Invoicify.company.model.Contact;
import com.awesometeam.Invoicify.invoice.model.Invoice;
import com.awesometeam.Invoicify.invoice.repository.InvoiceRepository;
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

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
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

    @Test
    void createNewInvoice() throws Exception{
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice invoice=new Invoice(company, new Date(2021,07,12)
                ,"Unpaid",new Date (2021,07,12) ,0.0, null );
        Map<String, Object> requestBody= new HashMap<>();
        requestBody.put("company", invoice.getCompany());
        requestBody.put("invoiceDate", invoice.getInvoiceDate());
        requestBody.put("status", invoice.getStatus());
        requestBody.put("modifiedDate", invoice.getModifiedDate());
        requestBody.put("cost", invoice.getCost());
        requestBody.put("invoiceDetails", null);

        doAnswer(invocation ->{
            Invoice inv=invocation.getArgument(0);
            inv.setInvoiceId(1);
            return null;
        }).when(invoiceRepository).save(isA(Invoice.class));

        mvc.perform(post("/createNewInvoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("invoiceId").value(1))
                .andExpect(jsonPath("cost").value(0.0));
    }

    @Test
    void findInvoiceByInvoiceIdTest() throws Exception{
        Contact contact = new Contact("Person1","Sales Rep","111-222-3333");
        Company company=new Company("ABC..inc","123 Street, Phoenix,AZ", contact);
        Invoice invoice=new Invoice(1,company, new Date(2021,07,12)
                ,"Unpaid",new Date (2021,07,12) ,1.0, null );

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

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

        mvc.perform(get("/invoiceByCompany/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].invoiceId").value(2))
                .andDo(document("Get Invoice By Company ID"
                        , requestFields(
                                fieldWithPath("ID").description("Company ID"),
                                fieldWithPath("PageNo").description("The current page. This field is optional and defaulted to Zero"),
                                fieldWithPath("PageSize").description("The number of invoices to be returned for the current page. This field is optional and defaulted to Ten"))
                        , responseFields(
                                fieldWithPath("id").description("Internal ID of the added invoice"))
                        )
                );
    }

}

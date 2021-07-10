package com.awesometeam.Invoicify.company.controller;

import com.awesometeam.Invoicify.company.model.Company;
import com.awesometeam.Invoicify.company.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanyController {

    CompanyService service;


    public CompanyController(CompanyService service) {
        this.service = service;
    }

    @PostMapping("/company")
    public ResponseEntity<Company> SaveCompany(@RequestBody Company company){
        service.Add(company);
        return new ResponseEntity(company, HttpStatus.CREATED);
    }

}

package com.awesometeam.Invoicify.company.controller;

import com.awesometeam.Invoicify.company.model.Company;
import com.awesometeam.Invoicify.company.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;


import java.security.Provider;

@RestController
public class CompanyController {


    CompanyService service;


    public CompanyController(CompanyService service) {
        this.service = service;
    }
    @PostMapping("/company")
    public ResponseEntity SaveCompany(@RequestBody Company company){
        String checkName= service.findByName(company.getName());
        if (checkName !=null)
            return new ResponseEntity("Company Name should be unique",HttpStatus.BAD_REQUEST);

        //service.Add(company);
        return new ResponseEntity(service.Add(company), HttpStatus.CREATED);
    }

    @GetMapping("/company")
    public ResponseEntity<Iterable<Company>>  findAllCompanies() {
        return new ResponseEntity(service.findAll(), HttpStatus.OK);
    }



    @PutMapping("/company/{Id}")
    public ResponseEntity UpdateCompany(@RequestBody Company company, @PathVariable long Id){
        if (service.findById(Id) == null) {
            return new ResponseEntity("Company Id not found",HttpStatus.BAD_REQUEST);
        }
        String checkName= service.findByName(company.getName());
        if (checkName !=null)
            return new ResponseEntity("Company Name should be unique",HttpStatus.BAD_REQUEST);

        //service.Add(company);
        return new ResponseEntity(service.Update(company), HttpStatus.OK);
    }

}

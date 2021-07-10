package com.awesometeam.Invoicify.company.service;

import com.awesometeam.Invoicify.company.model.Company;
import com.awesometeam.Invoicify.company.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository repo;

    public Company Add(Company company) {
        return repo.save(company);
    }
}

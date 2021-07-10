package com.awesometeam.Invoicify.Service;

import com.awesometeam.Invoicify.Domain.Company;
import com.awesometeam.Invoicify.Repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;


public class CompanyService {

    @Autowired
    CompanyRepository repo;

    public Company Add(Company company) {
        return repo.save(company);
    }
}

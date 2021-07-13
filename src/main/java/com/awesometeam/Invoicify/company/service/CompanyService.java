package com.awesometeam.Invoicify.company.service;

import com.awesometeam.Invoicify.company.model.Company;
import com.awesometeam.Invoicify.company.repository.CompanyRepository;
import com.awesometeam.Invoicify.company.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository repo;
    @Autowired
    ContactRepository contactRepo;

    public Company Add(Company company) {
        contactRepo.save(company.getContact());
        return repo.save(company);
    }

    public String findByName(String Name) {
        return repo.findByName(Name);
    }
}

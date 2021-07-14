package com.awesometeam.Invoicify.company.service;

import com.awesometeam.Invoicify.company.model.Company;
import com.awesometeam.Invoicify.company.repository.CompanyRepository;
import com.awesometeam.Invoicify.company.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository repo;
    @Autowired
    ContactRepository contactRepo;

    public Company Add(Company company) {
        if (company!=null && company.getContact()!=null)
            contactRepo.save(company.getContact());
        return repo.save(company);
    }

    public  Company Update(Company company) {
        Company existingCompany = repo.findById(company.getId()).get();
        if (existingCompany != null) {
            if (existingCompany.getContact() != null) {

                contactRepo.save(company.getContact());
            }
            repo.save(company);

        }
        return  existingCompany;
    }

    public String findByName(String Name) {
        return repo.findByName(Name);
    }

    public List<Company> findAll() { return repo.findAll();}

    public Company findById(long id) {
        return  repo.findById(id).orElse(null);
    }
}

package com.awesometeam.Invoicify.company.repository;

import com.awesometeam.Invoicify.company.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CompanyRepository extends JpaRepository<Company,Long> {

}

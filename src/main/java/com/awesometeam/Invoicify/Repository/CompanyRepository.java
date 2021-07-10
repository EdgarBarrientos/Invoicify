package com.awesometeam.Invoicify.Repository;

import com.awesometeam.Invoicify.Domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;


public interface CompanyRepository extends JpaRepository<Company,Long> {

}

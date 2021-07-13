package com.awesometeam.Invoicify.company.repository;

import com.awesometeam.Invoicify.company.model.Company;
import com.awesometeam.Invoicify.company.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface ContactRepository extends JpaRepository<Contact,Long> {

   /*
    @Transactional
    @Query("Select Name from Company p where p.Name = :Name")
    String findByName(@Param("Name") String Name);
*/

}

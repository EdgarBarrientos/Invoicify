package com.awesometeam.Invoicify.invoice.repository;

import com.awesometeam.Invoicify.invoice.model.Items;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsRepository extends JpaRepository<Items,Long>
{

}

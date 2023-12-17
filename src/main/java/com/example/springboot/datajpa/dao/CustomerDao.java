package com.example.springboot.datajpa.dao;

import com.example.springboot.datajpa.domain.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerDao extends CrudRepository<Customer, Long>, PagingAndSortingRepository<Customer, Long> {
    @Query("select c from Customer c left join fetch c.invoices i where c.id=?1")
    public Customer fetchByIdWithInvoice(Long id);
}

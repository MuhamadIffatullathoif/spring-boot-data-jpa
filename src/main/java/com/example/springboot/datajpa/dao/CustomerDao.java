package com.example.springboot.datajpa.dao;

import com.example.springboot.datajpa.domain.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerDao extends CrudRepository<Customer, Long>, PagingAndSortingRepository<Customer, Long> {
}

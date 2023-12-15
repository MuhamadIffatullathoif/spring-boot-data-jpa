package com.example.springboot.datajpa.dao;

import com.example.springboot.datajpa.domain.Customer;

import java.util.List;

public interface CustomerDao {
    public List<Customer> findAll();
    public void save(Customer customer);
    public Customer findOne(Long id);
    public void delete(Long id);
}

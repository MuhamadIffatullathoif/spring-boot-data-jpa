package com.example.springboot.datajpa.services;

import com.example.springboot.datajpa.domain.Customer;
import com.example.springboot.datajpa.domain.Invoice;
import com.example.springboot.datajpa.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    public List<Customer> findAll();
    public Page<Customer> findAll(Pageable pageable);
    public void save(Customer customer);
    public Customer findOne(Long id);
    public void delete(Long id);
    public List<Product> findByName(String term);
    public void saveInvoice(Invoice invoice);
}

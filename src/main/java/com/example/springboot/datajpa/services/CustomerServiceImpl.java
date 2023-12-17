package com.example.springboot.datajpa.services;

import com.example.springboot.datajpa.dao.CustomerDao;
import com.example.springboot.datajpa.dao.ProductDao;
import com.example.springboot.datajpa.domain.Customer;
import com.example.springboot.datajpa.domain.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private ProductDao productDao;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Transactional(readOnly = true)
    @Override
    public List<Customer> findAll() {
        return (List<Customer>) customerDao.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return customerDao.findAll(pageable);
    }

    @Transactional
    @Override
    public void save(Customer customer) {
        customerDao.save(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public Customer findOne(Long id) {
        return customerDao.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        customerDao.deleteById(id);
    }

    @Override
    public List<Product> findByName(String term) {
        // return productDao.findByName(term);
        return productDao.findByNameLikeIgnoreCase("%" + term + "%");
    }
}

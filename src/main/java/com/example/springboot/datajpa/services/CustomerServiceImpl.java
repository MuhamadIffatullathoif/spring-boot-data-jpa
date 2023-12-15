package com.example.springboot.datajpa.services;

import com.example.springboot.datajpa.dao.CustomerDao;
import com.example.springboot.datajpa.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerDao customerDao;
    @Transactional(readOnly = true)
    @Override
    public List<Customer> findAll() {
        return (List<Customer>) customerDao.findAll();
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
}

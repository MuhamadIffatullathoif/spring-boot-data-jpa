package com.example.springboot.datajpa.dao;

import com.example.springboot.datajpa.domain.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class CustomerDaoImpl implements CustomerDao{
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public List<Customer> findAll() {
        return entityManager.createQuery("from Customer").getResultList();
    }

    @Transactional
    @Override
    public void save(Customer customer) {
        if(customer.getId() != null && customer.getId() > 0) {
            entityManager.merge(customer);
        } else {
            entityManager.persist(customer);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Customer findOne(Long id) {
        return entityManager.find(Customer.class, id);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        entityManager.remove(findOne(id));
    }
}

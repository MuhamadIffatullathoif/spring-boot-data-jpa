package com.example.springboot.datajpa.dao;

import com.example.springboot.datajpa.domain.Invoice;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceDao extends CrudRepository<Invoice, Long> {
}

package com.example.springboot.datajpa.dao;

import com.example.springboot.datajpa.domain.Invoice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface InvoiceDao extends CrudRepository<Invoice, Long> {
    @Query("select i from Invoice i join fetch i.customer c join fetch i.items l join l.product where i.id=?1")
    public Invoice fetchByIdWithCustomerWithItemInvoiceWithProduct(Long id);
}

package com.example.springboot.datajpa.dao;

import com.example.springboot.datajpa.domain.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductDao extends CrudRepository<Product, Long> {
    @Query("select p from Product p where p.name like %?1%")
    public List<Product> findByName(String name);

    public List<Product> findByNameLikeIgnoreCase(String name);
}

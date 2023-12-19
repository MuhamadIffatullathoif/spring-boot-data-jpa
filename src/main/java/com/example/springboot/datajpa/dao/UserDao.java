package com.example.springboot.datajpa.dao;

import com.example.springboot.datajpa.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Long> {
    public User findByUsername(String username);
}

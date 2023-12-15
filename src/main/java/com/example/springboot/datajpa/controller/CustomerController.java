package com.example.springboot.datajpa.controller;

import com.example.springboot.datajpa.dao.CustomerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController {

    @Autowired
    private CustomerDao customerDao;

    @GetMapping("list")
    public String list(Model model) {
        model.addAttribute("title", "List of customers");
        model.addAttribute("customers", customerDao.findAll());
        return "list";
    }
}

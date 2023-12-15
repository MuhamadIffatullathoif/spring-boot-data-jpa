package com.example.springboot.datajpa.controller;

import com.example.springboot.datajpa.dao.CustomerDao;
import com.example.springboot.datajpa.domain.Customer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Map;
import java.util.Objects;

@Controller
@SessionAttributes("customer")
public class CustomerController {

    @Autowired
    private CustomerDao customerDao;

    @GetMapping("list")
    public String list(Model model) {
        model.addAttribute("title", "List of customers");
        model.addAttribute("customers", customerDao.findAll());
        return "list";
    }

    @RequestMapping(value = "/form")
    public String create(Map<String, Object> model) {
        Customer customer = new Customer();
        model.put("customer", customer);
        model.put("title", "Form of customer");
        return "form";
    }

    @RequestMapping(value = "/form/{id}")
    public String update(@PathVariable("id") Long id, Map<String, Object> model) {
        Customer customer = null;
        if(id > 0) {
            customer = customerDao.findOne(id);
        } else {
         return "redirect:/list";
        }
        model.put("title","Edit Customer");
        model.put("customer", customer);
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String save(@Valid Customer customer, BindingResult result, Model model, SessionStatus status) {
        if(result.hasErrors()) {
            model.addAttribute("title", "Form of customer");
            return "form";
        }
        customerDao.save(customer);
        status.setComplete();
        return "redirect:list";
    }
}

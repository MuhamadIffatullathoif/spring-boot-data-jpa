package com.example.springboot.datajpa.controller;

import com.example.springboot.datajpa.domain.Customer;
import com.example.springboot.datajpa.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Map;

@Controller
@SessionAttributes("customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("list")
    public String list(Model model) {
        model.addAttribute("title", "List of customers");
        model.addAttribute("customers", customerService.findAll());
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
            customer = customerService.findOne(id);
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
        customerService.save(customer);
        status.setComplete();
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{id}")
    public String delete(@PathVariable Long id){
        if(id > 0) {
            customerService.delete(id);
        }
        return "redirect:/list";
    }
}

package com.example.springboot.datajpa.controller;

import com.example.springboot.datajpa.domain.Customer;
import com.example.springboot.datajpa.domain.Invoice;
import com.example.springboot.datajpa.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/invoice")
@SessionAttributes("invoice")
public class InvoiceController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/form/{customerId}")
    public String create(@PathVariable(value = "customerId") Long customerId, Map<String, Object> model, RedirectAttributes flash) {
        Customer customer = customerService.findOne(customerId);
        if (customer == null) {
            flash.addFlashAttribute("error", "Customer not exists");
            return "redirect:/list";
        }
        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        model.put("invoice", invoice);
        model.put("title", "Create Invoice");
        return "invoice/form";
    }
}

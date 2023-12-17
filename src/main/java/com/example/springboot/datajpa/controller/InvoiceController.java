package com.example.springboot.datajpa.controller;

import com.example.springboot.datajpa.domain.Customer;
import com.example.springboot.datajpa.domain.Invoice;
import com.example.springboot.datajpa.domain.ItemInvoice;
import com.example.springboot.datajpa.domain.Product;
import com.example.springboot.datajpa.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
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

    @GetMapping(value = "/upload-products/{term}", produces = {"application/json"})
    public @ResponseBody List<Product> uploadProduct(@PathVariable String term) {
        return customerService.findByName(term);
    }

    @PostMapping("/form")
    public String save(@Valid Invoice invoice,
                       BindingResult result,
                       Model model,
                       @RequestParam(name = "item_id[]", required = false) Long[] itemId,
                       @RequestParam(name = "amount[]", required = false) Integer[] amount,
                       RedirectAttributes flash,
                       SessionStatus status) {
        if(result.hasErrors()) {
            model.addAttribute("title","Create Invoice");
            return "invoice/form";
        }

        if(itemId == null || itemId.length == 0) {
            model.addAttribute("title","Create Invoice");
            model.addAttribute("error", "Error: The invoice may not have lines");
            return "invoice/form";
        }
        for (int i = 0; i < itemId.length; i++) {
            Product product = customerService.findProductById(itemId[i]);

            ItemInvoice itemInvoice = new ItemInvoice();
            itemInvoice.setAmount(amount[i]);
            itemInvoice.setProduct(product);
            invoice.addItem(itemInvoice);
        }
        customerService.saveInvoice(invoice);
        status.setComplete();
        flash.addFlashAttribute("Success","Success save invoice");
        return "redirect:/ver/" + invoice.getCustomer().getId();
    }
}

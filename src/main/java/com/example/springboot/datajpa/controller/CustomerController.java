package com.example.springboot.datajpa.controller;

import com.example.springboot.datajpa.domain.Customer;
import com.example.springboot.datajpa.services.CustomerService;
import com.example.springboot.datajpa.util.paginator.PageRender;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@SessionAttributes("customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/list")
    public String list(@RequestParam(value = "page", defaultValue = "0")int page, Model model) {
        // implement paging and sorting
        Pageable pageRequest = PageRequest.of(page, 4);

        // implement Paginator custom
        Page<Customer> customers = customerService.findAll(pageRequest);
        PageRender<Customer> pageRender = new PageRender<Customer>("/list", customers);
        model.addAttribute("title", "List of customers");
        model.addAttribute("customers", customers);
        model.addAttribute("page", pageRender);
        // model.addAttribute("customers", customerService.findAll(pageRequest));
        // model.addAttribute("customers", customerService.findAll());
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
    public String save(@Valid Customer customer, BindingResult result, Model model, SessionStatus status, RedirectAttributes flash) {
        if(result.hasErrors()) {
            model.addAttribute("title", "Form of customer");
            return "form";
        }
        customerService.save(customer);
        flash.addFlashAttribute("success", "Customer saved successfully");
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

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Controller
@SessionAttributes("customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable("id") Long id, Map<String, Object> model, RedirectAttributes flash) {
        Customer customer = customerService.findOne(id);
        if (customer == null) {
            flash.addFlashAttribute("error", "customer id not found");
            return "redirect:/list";
        }
        model.put("customer", customer);
        model.put("title", "Detail Customer: " + customer.getName());
        return "ver";
    }

    @GetMapping("/list")
    public String list(@RequestParam(value = "page", defaultValue = "0") int page, Model model) {
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
        if (id > 0) {
            customer = customerService.findOne(id);
        } else {
            return "redirect:/list";
        }
        model.put("title", "Edit Customer");
        model.put("customer", customer);
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String save(@Valid Customer customer, BindingResult result, Model model, @RequestParam("file") MultipartFile photo, SessionStatus status, RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("title", "Form of customer");
            return "form";
        }

        if (!photo.isEmpty()) {
            // Path resourceDirectory = Paths.get("src//main//resources//static//uploads");
            // String rootPath = resourceDirectory.toFile().getAbsolutePath();

            // external uploads
            // String rootPath = "C://Temp//uploads";

            // absolute and external directory
            String uniqueFilename = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
            Path rootPath = Paths.get("uploads").resolve(uniqueFilename);
            Path rootAbsolutePath = rootPath.toAbsolutePath();
            try {
                // byte[] photoBytes = photo.getBytes();
                // Path fullRoute = Paths.get(rootPath + "//" + photo.getOriginalFilename());
                // Files.write(fullRoute, photoBytes);

                // absolute and external directory
                Files.copy(photo.getInputStream(), rootAbsolutePath);
                // flash.addFlashAttribute("success", "Success upload'" + photo.getOriginalFilename() + "'");
                flash.addFlashAttribute("success", "Success upload'" + uniqueFilename + "'");
                customer.setPhoto(uniqueFilename);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        customerService.save(customer);
        flash.addFlashAttribute("success", "Customer saved successfully");
        status.setComplete();
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{id}")
    public String delete(@PathVariable Long id) {
        if (id > 0) {
            customerService.delete(id);
        }
        return "redirect:/list";
    }
}

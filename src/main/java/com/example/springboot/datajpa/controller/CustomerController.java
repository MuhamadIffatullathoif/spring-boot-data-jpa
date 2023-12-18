package com.example.springboot.datajpa.controller;

import com.example.springboot.datajpa.domain.Customer;
import com.example.springboot.datajpa.services.CustomerService;
import com.example.springboot.datajpa.services.UploadFileService;
import com.example.springboot.datajpa.util.paginator.PageRender;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Map;

@Controller
@SessionAttributes("customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private UploadFileService uploadFileService;
    private final static String UPLOADS_FOLDER = "uploads";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Secured("ROLE_USER")
    @GetMapping("/uploads/{filename:.+}")
    public ResponseEntity<Resource> verPhoto(@PathVariable String filename) {
        // comment this to use service UploadFile
        // Path path = Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
        // Resource resource = null;
        // try {
        //    resource = new UrlResource(path.toUri());
        //    if (!resource.exists() && !resource.isReadable()) {
        //        throw new RuntimeException("Error cannot load image");
        //    }
        //} catch (MalformedURLException e) {
        //    throw new RuntimeException(e);
        //}
        Resource resource = null;
        try {
            // load image use service
            resource = uploadFileService.load(filename);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/ver/{id}")
    public String ver(@PathVariable("id") Long id, Map<String, Object> model, RedirectAttributes flash) {
        Customer customer = customerService.fetchByIdWithInvoice(id);
        if (customer == null) {
            flash.addFlashAttribute("error", "customer id not found");
            return "redirect:/list";
        }
        model.put("customer", customer);
        model.put("title", "Detail Customer: " + customer.getName());
        return "ver";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @GetMapping({"/list", "/"})
    public String list(@RequestParam(value = "page", defaultValue = "0") int page, Model model, Authentication authentication, HttpServletRequest request) {
        if (authentication != null) {
            logger.info("Hello authenticated user, your username is".concat(authentication.getName()));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            logger.info("FROM : (SecurityContextHolder.getContext().getAuthentication()) -> Hello authenticated user, your username is".concat(auth.getName()));
        }

        if (hasRole("ROLE_ADMIN")) {
            logger.info("HALO".concat(auth.getName()).concat("you have access"));
        } else {
            logger.info("HALO".concat(auth.getName()).concat("you dont have access"));
        }

        SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "");
        if(securityContext.isUserInRole("ROLE_ADMIN")) {
            logger.info("HALO FROM : SecurityContextHolderAwareRequestWrapper".concat(auth.getName()).concat("you have access"));
        } else {
            logger.info("HALO FROM: SecurityContextHolderAwareRequestWrapper".concat(auth.getName()).concat("you dont have access"));
        }

        if(request.isUserInRole("ROLE_ADMIN")) {
            logger.info("HALO FROM : HttpServletRequest".concat(auth.getName()).concat("you have access"));
        } else {
            logger.info("HALO FROM: HttpServletRequest".concat(auth.getName()).concat("you dont have access"));
        }
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
            // delete photo when upload again
            if (customer.getId() != null && customer.getId() > 0 && customer.getPhoto() != null && customer.getPhoto().length() > 0) {
//                Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(customer.getPhoto()).toAbsolutePath();
//                File archive = rootPath.toFile();
//
//                if (archive.exists() && archive.canRead()) {
//                    boolean isDelete = archive.delete();
//                    if (isDelete) {
//                        System.out.println("Success Delete Photo" + customer.getPhoto());
//                    }
//                }
                // delete image use service
                uploadFileService.delete(customer.getPhoto());
            }

            // Path resourceDirectory = Paths.get("src//main//resources//static//uploads");
            // String rootPath = resourceDirectory.toFile().getAbsolutePath();

            // external uploads
            // String rootPath = "C://Temp//uploads";

            // absolute and external directory
//            String uniqueFilename = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
//            Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(uniqueFilename);
//            Path rootAbsolutePath = rootPath.toAbsolutePath();
//            try {
//                // byte[] photoBytes = photo.getBytes();
//                // Path fullRoute = Paths.get(rootPath + "//" + photo.getOriginalFilename());
//                // Files.write(fullRoute, photoBytes);
//
//                // absolute and external directory
//                Files.copy(photo.getInputStream(), rootAbsolutePath);
//                // flash.addFlashAttribute("success", "Success upload'" + photo.getOriginalFilename() + "'");
//                flash.addFlashAttribute("success", "Success upload'" + uniqueFilename + "'");
//                customer.setPhoto(uniqueFilename);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }

            // copy image to folder uploads use service
            String uniqueFilename = null;
            try {
                uniqueFilename = uploadFileService.copy(photo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            flash.addFlashAttribute("success", "Success upload'" + uniqueFilename + "'");
            customer.setPhoto(uniqueFilename);
        }
        customerService.save(customer);
        flash.addFlashAttribute("success", "Customer saved successfully");
        status.setComplete();
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{id}")
    public String delete(@PathVariable Long id) {
        if (id > 0) {
            // get data for deleting image
            Customer customer = customerService.findOne(id);
            customerService.delete(id);
            if (uploadFileService.delete(customer.getPhoto())) {
                System.out.println("Success Delete Photo" + customer.getPhoto());
            }
        }
        return "redirect:/list";
    }

    private boolean hasRole(String role) {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return false;
        }

        Authentication authentication = context.getAuthentication();

        if (authentication == null) {
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.contains(new SimpleGrantedAuthority(role));
//        for (GrantedAuthority authority : authorities) {
//            if (role.equals(authority.getAuthority())) {
//                logger.info("HALO".concat(authentication.getName()).concat("you have access").concat("ROLE: ".concat(authority.getAuthority())));
//                return true;
//            }
//        }

//        return false;
    }
}

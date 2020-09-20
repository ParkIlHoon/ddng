package com.ddng.adminuibootstrap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
public class CustomerController
{
    @GetMapping("/search")
    public String searchCustomerForm (Model model)
    {
        return "customer/index";
    }

    @GetMapping("/new")
    public String newCustomerForm (Model model)
    {
        return "customer/new-customer";
    }

    @GetMapping("/family")
    public String familyForm (Model model)
    {
        return "customer/family";
    }
}

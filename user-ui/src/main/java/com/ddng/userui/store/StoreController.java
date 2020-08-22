package com.ddng.userui.store;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StoreController
{
    @GetMapping("/store")
    public String main (Model model)
    {
        return "store/main";
    }
}

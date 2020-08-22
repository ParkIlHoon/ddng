package com.ddng.userui.hotel;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HotelController
{
    @GetMapping("/hotel")
    public String main (Model model)
    {
        return "hotel/main";
    }
}

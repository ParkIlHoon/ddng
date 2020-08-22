package com.ddng.userui.kindergarden;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KinderController
{
    @GetMapping("/kindergarden")
    public String main (Model model)
    {
        return "kindergarden/main";
    }
}

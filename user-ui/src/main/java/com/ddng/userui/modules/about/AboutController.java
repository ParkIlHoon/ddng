package com.ddng.userui.modules.about;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController
{
    @GetMapping("/about")
    public String main (Model model)
    {
        return "about/main";
    }
}

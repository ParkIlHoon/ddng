package com.ddng.userui.modules.beauty;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class BeautyController
{
    @GetMapping("/beauty")
    public String main (Model model)
    {
        return "beauty/main";
    }
}

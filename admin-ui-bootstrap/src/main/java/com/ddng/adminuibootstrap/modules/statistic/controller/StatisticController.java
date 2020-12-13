package com.ddng.adminuibootstrap.modules.statistic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/statistic")
public class StatisticController
{
    @GetMapping("/calculate")
    public String calculateForm(Model model)
    {
        return "statistic/calculate";
    }
}

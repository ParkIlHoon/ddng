package com.ddng.adminuibootstrap.modules.schedules.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/schedule")
public class ScheduleController
{

    @GetMapping("/main")
    public String mainForm (Model model)
    {
        return "schedule/main";
    }
}

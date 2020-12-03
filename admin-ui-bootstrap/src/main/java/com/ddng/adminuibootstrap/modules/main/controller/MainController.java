package com.ddng.adminuibootstrap.modules.main.controller;

import com.ddng.adminuibootstrap.modules.schedules.template.ScheduleTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
public class MainController
{
    private final ScheduleTemplate scheduleTemplate;

    @GetMapping("/")
    public String main (Model model)
    {
        String searchDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-DD"));
        //scheduleTemplate.getSchedule(searchDate, searchDate);

        return "index";
    }

    @GetMapping("/login")
    public String loginPage (Model model)
    {
        return "login";
    }

}

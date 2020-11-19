package com.ddng.adminuibootstrap.modules.main.controller;

import com.ddng.adminuibootstrap.modules.main.dto.ScheduleDto;
import com.ddng.adminuibootstrap.modules.main.utils.ScheduleRestTemplateClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class MainController
{
    @Autowired
    ScheduleRestTemplateClient scheduleRestTemplateClient;

    @GetMapping("/")
    public String main (Model model)
    {
        String searchDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-DD"));
        //scheduleRestTemplateClient.getSchedule(searchDate, searchDate);

        return "index";
    }

    @GetMapping("/login")
    public String loginPage (Model model)
    {
        return "login";
    }

    @GetMapping("/getSchedules")
    public String getSchedules (String searchDate)
    {
        scheduleRestTemplateClient.getSchedule(searchDate, searchDate);
        return null;
    }
}

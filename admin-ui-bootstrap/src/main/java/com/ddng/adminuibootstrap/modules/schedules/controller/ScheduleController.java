package com.ddng.adminuibootstrap.modules.schedules.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <h1>예약 관리 메뉴 컨트롤러</h1>
 */
@Controller
@RequestMapping("/schedule")
public class ScheduleController
{
    /**
     * 스케쥴 관리 메뉴 폼 요청
     * @param model
     * @return
     */
    @GetMapping("/main")
    public String mainForm (Model model)
    {
        return "schedule/main";
    }
}

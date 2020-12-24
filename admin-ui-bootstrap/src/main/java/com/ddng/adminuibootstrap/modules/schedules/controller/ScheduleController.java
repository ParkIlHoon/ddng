package com.ddng.adminuibootstrap.modules.schedules.controller;

import com.ddng.adminuibootstrap.infra.RestPageImpl;
import com.ddng.adminuibootstrap.modules.customer.dto.CustomerDto;
import com.ddng.adminuibootstrap.modules.customer.dto.CustomerTagDto;
import com.ddng.adminuibootstrap.modules.customer.dto.CustomerTypeDto;
import com.ddng.adminuibootstrap.modules.customer.form.EditForm;
import com.ddng.adminuibootstrap.modules.customer.template.CustomerTemplate;
import com.ddng.adminuibootstrap.modules.schedules.dto.ScheduleDto;
import com.ddng.adminuibootstrap.modules.schedules.dto.ScheduleTagDto;
import com.ddng.adminuibootstrap.modules.schedules.dto.ScheduleTypeDto;
import com.ddng.adminuibootstrap.modules.schedules.template.ScheduleTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>예약 관리 메뉴 컨트롤러</h1>
 */
@Controller
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController
{
    private final ScheduleTemplate scheduleTemplate;
    private final CustomerTemplate customerTemplate;

    /**
     * 스케쥴 관리 메뉴 폼 요청
     * @param model
     * @return
     */
    @GetMapping("/main")
    public String mainForm (Model model)
    {
        List<ScheduleTypeDto> scheduleTypes = scheduleTemplate.getScheduleTypes();
        List<ScheduleTagDto> scheduleTags = scheduleTemplate.getScheduleTags();

        model.addAttribute("scheduleTypes", scheduleTypes);
        model.addAttribute("scheduleTags", scheduleTags);
        return "schedule/main";
    }

    /**
     * 스케쥴 조회 요청
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/search")
    public ResponseEntity searchSchedules (String startDate, String endDate)
    {
        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate))
        {
            return ResponseEntity.badRequest().build();
        }

        List<ScheduleDto> schedules = scheduleTemplate.getSchedule(startDate, endDate);
        return ResponseEntity.ok(schedules);
    }

    /**
     * 고객 목록 조회 요청
     * @param keyword 조회 키워드
     * @return
     */
    @GetMapping("/customers")
    public ResponseEntity searchAction (String keyword, int page, int size)
    {
        if (StringUtils.hasText(keyword))
        {
            RestPageImpl<CustomerDto> customersWithPage = customerTemplate.searchCustomersWithPage(keyword, page, size);
            return ResponseEntity.ok(customersWithPage);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * 고객 조회 요청
     * @param id 고객 아이디
     * @return
     */
    @GetMapping("/customers/{id}")
    public ResponseEntity searchCustomerAction (@PathVariable("id") Long id)
    {
        if (id == null)
        {
            return ResponseEntity.badRequest().build();
        }

        CustomerDto customer = customerTemplate.getCustomer(id);
        return ResponseEntity.ok(customer);
    }

}

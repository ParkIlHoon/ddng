package com.ddng.adminuibootstrap.modules.schedules.controller;

import com.ddng.adminuibootstrap.modules.common.clients.CustomerClient;
import com.ddng.adminuibootstrap.modules.common.clients.ScheduleClient;
import com.ddng.adminuibootstrap.modules.common.dto.FeignPageImpl;
import com.ddng.adminuibootstrap.modules.common.dto.customer.CustomerDto;
import com.ddng.adminuibootstrap.modules.common.dto.schedule.ScheduleTagDto;
import com.ddng.adminuibootstrap.modules.common.dto.schedule.ScheduleTypeDto;
import com.ddng.adminuibootstrap.modules.schedules.form.ScheduleForm;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <h1>예약 관리 메뉴 컨트롤러</h1>
 */
@Controller
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleClient scheduleClient;
    private final CustomerClient customerClient;

    /**
     * 스케쥴 관리 메뉴 폼 요청
     */
    @GetMapping("/main")
    public String mainForm(Model model) {
        List<ScheduleTypeDto> scheduleTypes = scheduleClient.getScheduleTypes();
        List<ScheduleTagDto> scheduleTags = scheduleClient.getScheduleTags();

        model.addAttribute("scheduleTypes", scheduleTypes);
        model.addAttribute("scheduleTags", scheduleTags);
        model.addAttribute("scheduleForm", new ScheduleForm());
        return "schedule/main";
    }

    /**
     * 스케쥴 조회 요청
     */
    @GetMapping("/search")
    public ResponseEntity searchSchedules(@NotBlank String startDate, @NotBlank String endDate) {
        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(scheduleClient.getSchedules(startDate, endDate));
    }

    /**
     * 고객 목록 조회 요청
     *
     * @param keyword 조회 키워드
     */
    @GetMapping("/customers")
    public ResponseEntity searchAction(String keyword, @Min(0) int page, @Min(1) int size) {
        if (StringUtils.hasText(keyword)) {
            FeignPageImpl<CustomerDto> customersWithPage = customerClient.searchCustomersWithPage(keyword, page, size);
            return ResponseEntity.ok(customersWithPage);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * 고객 조회 요청
     *
     * @param id 고객 아이디
     */
    @GetMapping("/customers/{id}")
    public ResponseEntity searchCustomerAction(@PathVariable("id") Long id) {
        CustomerDto customer = customerClient.getCustomer(id);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    /**
     * 스케쥴 신규 생성 요청
     *
     * @param scheduleForm 생성할 내용
     */
    @PostMapping("/add")
    public ResponseEntity addScheduleAction(@Valid
                                            @RequestBody ScheduleForm scheduleForm,
                                            Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        return scheduleClient.createSchedule(scheduleForm);
    }

    /**
     * 스케쥴 수정 요청
     *
     * @param id           스케쥴 아이디
     * @param scheduleForm 수정할 내용
     */
    @PostMapping("/update/{id}")
    public ResponseEntity updateScheduleAction(@PathVariable("id") Long id,
                                                @Valid
                                                @RequestBody ScheduleForm scheduleForm,
                                                Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        return scheduleClient.updateSchedule(id, scheduleForm);
    }

    /**
     * 스케쥴 제거 요청
     *
     * @param id 스케쥴 아이디
     */
    @PostMapping("/remove/{id}")
    public ResponseEntity removeScheduleAction(@PathVariable("id") Long id) {
        return scheduleClient.deleteSchedule(id);
    }
}

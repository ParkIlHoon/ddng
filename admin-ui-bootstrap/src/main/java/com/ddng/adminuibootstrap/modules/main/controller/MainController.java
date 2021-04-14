package com.ddng.adminuibootstrap.modules.main.controller;

import com.ddng.adminuibootstrap.modules.common.clients.CustomerClient;
import com.ddng.adminuibootstrap.modules.common.clients.ScheduleClient;
import com.ddng.adminuibootstrap.modules.common.dto.customer.CustomerDto;
import com.ddng.adminuibootstrap.modules.common.dto.schedule.ScheduleDto;
import com.ddng.adminuibootstrap.modules.common.dto.schedule.ScheduleType;
import com.ddng.adminuibootstrap.modules.main.form.MainScheduleForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController
{
    private final CustomerClient customerClient;
    private final ScheduleClient scheduleClient;

    @GetMapping("/")
    public String main (Model model)
    {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage (Model model)
    {
        return "login";
    }

    /**
     * 특정 일자의 호텔/유치원 스케쥴 조회
     * @param baseDate 조회 일자
     * @return
     */
    @GetMapping("/today-schedules")
    public ResponseEntity getTodaySchedules(String baseDate)
    {
        List<MainScheduleForm> forms = new ArrayList<>();
        List<ScheduleDto> schedules = scheduleClient.getCertainDaySchedule(baseDate);

        for (ScheduleDto schedule : schedules)
        {
            if(schedule.getScheduleType().equals(ScheduleType.HOTEL) || schedule.getScheduleType().equals(ScheduleType.KINDERGARTEN))
            {
                if (schedule.getCustomerId() != null)
                {
                    CustomerDto customer = customerClient.getCustomer(schedule.getCustomerId());
                    if (customer != null)
                    {
                        MainScheduleForm form = new MainScheduleForm(schedule, customer);
                        forms.add(form);
                    }
                }
            }
        }

        return ResponseEntity.ok(forms);
    }

}

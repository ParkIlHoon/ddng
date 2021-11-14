package com.ddng.adminuibootstrap.modules.main.controller;

import com.ddng.adminuibootstrap.modules.common.clients.CustomerClient;
import com.ddng.adminuibootstrap.modules.common.clients.ScheduleClient;
import com.ddng.adminuibootstrap.modules.common.dto.customer.CustomerDto;
import com.ddng.adminuibootstrap.modules.common.dto.schedule.ScheduleDto;
import com.ddng.adminuibootstrap.modules.common.dto.schedule.ScheduleType;
import com.ddng.adminuibootstrap.modules.main.form.MainScheduleForm;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final CustomerClient customerClient;
    private final ScheduleClient scheduleClient;

    @GetMapping("/")
    public String main() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * 특정 일자의 호텔/유치원 스케쥴 조회
     *
     * @param baseDate 조회 일자
     */
    @GetMapping("/today-schedules")
    public ResponseEntity getTodaySchedules(String baseDate) {
        List<MainScheduleForm> forms = new ArrayList<>();
        List<ScheduleDto> schedules = scheduleClient.getCertainDaySchedule(baseDate);

        List<Long> customerIds = schedules.stream()
            .filter(s -> (s.getScheduleType() == ScheduleType.HOTEL || s.getScheduleType() == ScheduleType.KINDERGARTEN) && s.getCustomerId() != null)
            .map(ScheduleDto::getCustomerId)
            .distinct()
            .collect(Collectors.toList());

        Map<Long, CustomerDto> customerDtoMap = customerClient.getCustomers(customerIds).stream()
            .collect(Collectors.toMap(CustomerDto::getId, Function.identity(), (o, n) -> o));

        schedules.stream()
            .filter(s -> (s.getScheduleType() == ScheduleType.HOTEL || s.getScheduleType() == ScheduleType.KINDERGARTEN) && s.getCustomerId() != null)
            .forEach(s -> {
                if (customerDtoMap.containsKey(s.getCustomerId())) {
                    forms.add(new MainScheduleForm(s, customerDtoMap.get(s.getCustomerId())));
                }
            });

        return ResponseEntity.ok(forms);
    }

}

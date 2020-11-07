package com.ddng.scheduleapi.modules.schedules.controller;

import com.ddng.scheduleapi.modules.schedules.domain.CalendarType;
import com.ddng.scheduleapi.modules.schedules.domain.ScheduleType;
import com.ddng.scheduleapi.modules.schedules.dto.SchedulesDto;
import com.ddng.scheduleapi.modules.schedules.service.SchedulesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h1>스케쥴 관련 요청 처리 컨트롤러 클래스</h1>
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class SchedulesController
{
    private final SchedulesService schedulesService;

    /**
     * 스케쥴을 조회한다.
     * @param baseDate 기준 일자
     * @param calendarType 캘린더 유형
     * @return
     */
    @GetMapping
    public ResponseEntity getSchedules (String baseDate, CalendarType calendarType)
    {
        if (!StringUtils.hasText(baseDate) || calendarType == null)
        {
            return ResponseEntity.badRequest().build();
        }

        List<SchedulesDto.Response> responses = schedulesService.getSchedules(baseDate, calendarType);

        return ResponseEntity.ok(responses);
    }

    /**
     * 스케쥴 타입을 조회한다.
     * @return
     */
    @GetMapping("/types")
    public ResponseEntity getScheduleTypes ()
    {
        List returnList = new ArrayList();
        for (ScheduleType type : ScheduleType.values())
        {
            Map row = new HashMap();
            row.put("id", type.name());
            row.put("name", type.getName());
            row.put("color", type.getColor());

            returnList.add(row);
        }
        return ResponseEntity.ok(returnList);
    }
}

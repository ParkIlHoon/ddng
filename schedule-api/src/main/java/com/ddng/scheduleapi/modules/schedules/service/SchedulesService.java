package com.ddng.scheduleapi.modules.schedules.service;

import com.ddng.scheduleapi.modules.schedules.domain.CalendarType;
import com.ddng.scheduleapi.modules.schedules.dto.SchedulesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SchedulesService
{
    public Page<SchedulesDto.Response> getSchedules(String baseDate, CalendarType calendarType)
    {
        return null;
    }
}

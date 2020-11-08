package com.ddng.scheduleapi.modules.schedules.service;

import com.ddng.scheduleapi.modules.schedules.domain.CalendarType;
import com.ddng.scheduleapi.modules.schedules.domain.Schedules;
import com.ddng.scheduleapi.modules.schedules.dto.SchedulesDto;
import com.ddng.scheduleapi.modules.schedules.repository.SchedulesRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SchedulesService
{
    private final SchedulesRepository schedulesRepository;
    private final ModelMapper modelMapper;

    public List<SchedulesDto.Response> getSchedules(String baseDate, CalendarType calendarType)
    {
        LocalDate parse = LocalDate.parse(baseDate);

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        switch (calendarType)
        {
            case DAILY:
                startDate = parse.atStartOfDay();
                endDate = startDate.plusDays(1);
                break;
            case WEEKLY:
                startDate = parse.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
                endDate = parse.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)).atStartOfDay().with(LocalTime.MAX);
                break;
            case MONTHLY:
                startDate = parse.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
                endDate = parse.with(TemporalAdjusters.lastDayOfMonth()).atStartOfDay().with(LocalTime.MAX);
                break;
            case TWO_WEEKS:
                startDate = parse.atStartOfDay();
                endDate = parse.plusWeeks(2).atStartOfDay().with(LocalTime.MAX);
                break;
            case THREE_WEEKS:
                startDate = parse.atStartOfDay();
                endDate = parse.plusWeeks(3).atStartOfDay().with(LocalTime.MAX);
                break;
        }

        List<Schedules> schedules = schedulesRepository.findByStartDateGreaterThanEqualAndEndDateLessThanEqual(startDate, endDate);
        return schedules.stream().map(s -> new SchedulesDto.Response(s)).collect(Collectors.toList());
    }

    public Optional<Schedules> getSchedule(Long id)
    {
        return schedulesRepository.findById(id);
    }

    public Schedules createSchedule(SchedulesDto.Post dto)
    {
        Schedules schedules = new Schedules();
        schedules.setName(dto.getName());
        schedules.setType(dto.getScheduleType());
        schedules.setAllDay(dto.isAllDay());
        schedules.setStartDate(LocalDateTime.parse(dto.getStartDate()));
        schedules.setEndDate(LocalDateTime.parse(dto.getEndDate()));
        schedules.setCustomerId(dto.getCustomerId());
        schedules.setUserId(dto.getUserId());
        schedules.setBigo(dto.getBigo());
        schedules.setPayed(dto.isPayed());

        return schedulesRepository.save(schedules);
    }


    public Schedules updateSchedule(Schedules schedules, SchedulesDto.Put dto)
    {
        schedules.setName(dto.getName());
        schedules.setType(dto.getScheduleType());
        schedules.setAllDay(dto.isAllDay());
        schedules.setStartDate(LocalDateTime.parse(dto.getStartDate()));
        schedules.setEndDate(LocalDateTime.parse(dto.getEndDate()));
        schedules.setCustomerId(dto.getCustomerId());
        schedules.setUserId(dto.getUserId());
        schedules.setBigo(dto.getBigo());
        schedules.setPayed(dto.isPayed());

        return schedulesRepository.save(schedules);
    }

    public void deleteSchedule(Schedules schedules)
    {
        schedulesRepository.delete(schedules);
    }
}

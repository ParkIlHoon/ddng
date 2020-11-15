package com.ddng.scheduleapi.modules.schedules.service;

import com.ddng.scheduleapi.modules.schedules.domain.CalendarType;
import com.ddng.scheduleapi.modules.schedules.domain.Schedules;
import com.ddng.scheduleapi.modules.schedules.dto.SchedulesDto;
import com.ddng.scheduleapi.modules.schedules.repository.SchedulesRepository;
import com.ddng.scheduleapi.modules.tag.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public List<SchedulesDto.Response> getSchedules(String startDate, String endDate, CalendarType calendarType)
    {
        LocalDateTime startDateTime = LocalDate.parse(startDate).atTime(LocalTime.MIN);
        LocalDateTime endDateTime = LocalDate.parse(endDate).atTime(LocalTime.MAX);

        List<Schedules> schedules = schedulesRepository.findByStartDateGreaterThanEqualAndEndDateLessThanEqual(startDateTime, endDateTime);
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

    public void addTag(Schedules schedules, Tag tag)
    {
        schedules.getTags().add(tag);
    }

    public void removeTag(Schedules schedules, Tag tag)
    {
        schedules.getTags().remove(tag);
    }
}

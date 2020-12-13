package com.ddng.scheduleapi.modules.schedules.service;

import com.ddng.scheduleapi.modules.schedules.domain.Schedules;
import com.ddng.scheduleapi.modules.schedules.dto.SchedulesDto;
import com.ddng.scheduleapi.modules.schedules.repository.SchedulesRepository;
import com.ddng.scheduleapi.modules.tag.domain.Tag;
import com.ddng.scheduleapi.modules.tag.dto.TagDto;
import com.ddng.scheduleapi.modules.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SchedulesService
{
    private final SchedulesRepository schedulesRepository;
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    public List<SchedulesDto.Response> getSchedules(String startDate, String endDate)
    {
        LocalDateTime startDateTime = LocalDate.parse(startDate).atTime(LocalTime.MIN);
        LocalDateTime endDateTime = LocalDate.parse(endDate).atTime(LocalTime.MAX);

        List<Schedules> schedules = schedulesRepository.getSchedulesForDuration(startDateTime, endDateTime);

        return schedules.stream().map(s -> new SchedulesDto.Response(s)).collect(Collectors.toList());
    }

    public List<SchedulesDto.Response> searchSchedules(SchedulesDto.Get dto)
    {
        List<Schedules> schedules = schedulesRepository.searchSchedules(dto);
        return schedules.stream().map(s -> new SchedulesDto.Response(s)).collect(Collectors.toList());
    }

    public List<SchedulesDto.Response> getCertainDaySchedules(SchedulesDto.Get dto)
    {
//        LocalDateTime startDateTime = LocalDate.parse(dto.getStartDate()).atTime(LocalTime.MAX);
//        LocalDateTime endDateTime = LocalDate.parse(dto.getEndDate()).atTime(LocalTime.MIN);

//        List<Schedules> schedules = schedulesRepository.findByStartDateLessThanEqualAndEndDateGreaterThanEqual(startDateTime, endDateTime);
        List<Schedules> schedules = schedulesRepository.getCertainDaysSchedules(dto);
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

        Schedules save = schedulesRepository.save(schedules);

        for (TagDto tagDto : dto.getTags())
        {
            Tag tag = null;
            Optional<Tag> optionalTag = tagRepository.findByTitle(tagDto.getTitle());
            if (optionalTag.isEmpty())
            {
                Tag newTag = new Tag();
                newTag.setTitle(tagDto.getTitle());
                tag = tagRepository.save(newTag);
            }
            else
            {
                tag = optionalTag.get();
            }

            addTag(save, tag);
        }

        return save;
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

        Schedules save = schedulesRepository.save(schedules);

        save.setTags(new HashSet<>());
        for (TagDto tagDto : dto.getTags())
        {
            Tag tag = null;
            Optional<Tag> optionalTag = tagRepository.findByTitle(tagDto.getTitle());
            if (optionalTag.isEmpty())
            {
                Tag newTag = new Tag();
                newTag.setTitle(tagDto.getTitle());
                tag = tagRepository.save(newTag);
            }
            else
            {
                tag = optionalTag.get();
            }

            addTag(save, tag);
        }

        return save;
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

    public void paySchedule(Long scheduleId)
    {
        Optional<Schedules> optional = this.getSchedule(scheduleId);
        if (optional.isPresent())
        {
            optional.get().setPayed(true);
        }
    }
}

package com.ddng.scheduleapi.modules.schedules.controller;

import com.ddng.scheduleapi.modules.schedules.domain.CalendarType;
import com.ddng.scheduleapi.modules.schedules.domain.ScheduleType;
import com.ddng.scheduleapi.modules.schedules.domain.Schedules;
import com.ddng.scheduleapi.modules.schedules.dto.SchedulesDto;
import com.ddng.scheduleapi.modules.schedules.service.SchedulesService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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
     * 특정 스케쥴을 조회한다.
     * @param id 조회할 스케쥴 아이디
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity getSchedule(@PathVariable("id") Long id)
    {
        Optional<Schedules> optionalSchedules = schedulesService.getSchedule(id);

        if (optionalSchedules.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new SchedulesDto.Response(optionalSchedules.get()));
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

    /**
     * 스케쥴을 생성한다.
     * @param dto 생성할 스케쥴 정보
     * @param errors
     * @return
     */
    @PostMapping
    public ResponseEntity createSchedule (@RequestBody @Valid SchedulesDto.Post dto,
                                          Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.badRequest().build();
        }

        Schedules created = schedulesService.createSchedule(dto);

        WebMvcLinkBuilder builder = linkTo(SchedulesController.class).slash(created.getId());
        return ResponseEntity.created(builder.toUri()).build();
    }

    /**
     * 스케쥴을 수정한다.
     * @param id 수정할 스케쥴의 아이디
     * @param dto 수정할 내용
     * @param errors
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity updateSchedule (@PathVariable("id") Long id,
                                          @RequestBody @Valid SchedulesDto.Put dto,
                                          Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.badRequest().build();
        }

        Optional<Schedules> optionalSchedules = schedulesService.getSchedule(id);
        if (optionalSchedules.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }

        Schedules updated = schedulesService.updateSchedule(optionalSchedules.get(), dto);
        return ResponseEntity.ok(new SchedulesDto.Response(updated));
    }


}

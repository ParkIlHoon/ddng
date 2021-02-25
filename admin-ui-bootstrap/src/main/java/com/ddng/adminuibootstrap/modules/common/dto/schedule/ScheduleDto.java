package com.ddng.adminuibootstrap.modules.common.dto.schedule;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * <h1>ddng-schedule-api 스케쥴 DTO</h1>
 */
@Data
@NoArgsConstructor @AllArgsConstructor
public class ScheduleDto
{
    private Long id;
    private String name;
    private ScheduleType scheduleType;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;
    private boolean allDay;
    private Long customerId;
    private Long userId;
    private String bigo;
    private boolean payed;
    private Set<ScheduleTagDto> tags = new HashSet<>();

    private String scheduleTypeName;
    private String scheduleColor;
}

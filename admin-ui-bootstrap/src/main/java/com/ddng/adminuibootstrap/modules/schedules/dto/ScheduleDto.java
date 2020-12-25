package com.ddng.adminuibootstrap.modules.schedules.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ScheduleDto
{
    private Long id;
    private String name;
    private String scheduleType;
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

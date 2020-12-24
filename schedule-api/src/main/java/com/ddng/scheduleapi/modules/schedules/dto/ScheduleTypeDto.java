package com.ddng.scheduleapi.modules.schedules.dto;

import com.ddng.scheduleapi.modules.schedules.domain.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ScheduleTypeDto
{
    private String id;
    private String name;
    private String color;

    public ScheduleTypeDto(ScheduleType type)
    {
        this.id = type.name();
        this.name = type.getName();
        this.color = type.getColor();
    }
}

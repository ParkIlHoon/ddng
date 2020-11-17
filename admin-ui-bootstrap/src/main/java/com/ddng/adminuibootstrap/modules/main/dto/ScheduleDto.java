package com.ddng.adminuibootstrap.modules.main.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class ScheduleDto
{
    private String scheduleTypeName;
    private Set<ScheduleTagDto> scheduleTags = new HashSet<>();
}

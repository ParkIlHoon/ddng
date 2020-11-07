package com.ddng.scheduleapi.modules.schedules;

import lombok.Getter;

@Getter
public enum ScheduleType
{
    BEAUTY("미용", "#00a9ff"),
    HOTEL("호텔", "#ff5583"),
    KINDERGARTEN("유치원", "#03bd9e"),
    VACATION("휴가", "#bbdc00"),
    ETC("기타", "#9d9d9d");

    private String name;
    private String color;

    ScheduleType(String name, String color)
    {
        this.name = name;
        this.color = color;
    }
}

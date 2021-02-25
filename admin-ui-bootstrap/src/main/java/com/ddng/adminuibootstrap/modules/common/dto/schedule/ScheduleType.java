package com.ddng.adminuibootstrap.modules.common.dto.schedule;

import lombok.Getter;

/**
 * <h1>스케쥴 타입 Enum 클래스</h1>
 *
 * @version 1.0
 */
@Getter
public enum ScheduleType
{
    BEAUTY("미용", "#00a9ff"),
    HOTEL("호텔", "#ff5583"),
    KINDERGARTEN("유치원", "#ffbb3b"),
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

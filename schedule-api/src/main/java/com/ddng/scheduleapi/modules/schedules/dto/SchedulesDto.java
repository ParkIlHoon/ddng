package com.ddng.scheduleapi.modules.schedules.dto;


import com.ddng.scheduleapi.modules.schedules.domain.ScheduleType;
import com.ddng.scheduleapi.modules.schedules.domain.Schedules;

import java.time.LocalDateTime;

public class SchedulesDto
{

    public static class Response
    {
        private Long id;
        private String name;
        private ScheduleType scheduleType;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private boolean isAllDay;
        private Long customerId;
        private Long userId;
        private String bigo;
        private boolean payed;

        private String scheduleTypeName;
        private String scheduleColor;

        public Response(Schedules schedules)
        {
            this.id = schedules.getId();
            this.name = schedules.getName();
            this.scheduleType = schedules.getType();
            this.startDate = schedules.getStartDate();
            this.endDate = schedules.getEndDate();
            this.isAllDay = schedules.isAllDay();
            this.customerId = schedules.getCustomerId();
            this.userId = schedules.getUserId();
            this.bigo = schedules.getBigo();
            this.payed = schedules.isPayed();

            this.scheduleTypeName = this.scheduleType.getName();
            this.scheduleColor = this.scheduleType.getColor();
        }
    }
}

package com.ddng.scheduleapi.modules.schedules.dto;


import com.ddng.scheduleapi.modules.schedules.domain.ScheduleType;
import com.ddng.scheduleapi.modules.schedules.domain.Schedules;
import com.ddng.scheduleapi.modules.tag.domain.Tag;
import com.ddng.scheduleapi.modules.tag.dto.TagDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class SchedulesDto
{
    @NoArgsConstructor @AllArgsConstructor
    @Getter @Setter
    public static class Response
    {
        private Long id;
        private String name;
        private ScheduleType scheduleType;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private boolean allDay;
        private Long customerId;
        private Long userId;
        private String bigo;
        private boolean payed;
        private Set<Tag> tags = new HashSet<>();

        private String scheduleTypeName;
        private String scheduleColor;

        public Response(Schedules schedules)
        {
            this.id = schedules.getId();
            this.name = schedules.getName();
            this.scheduleType = schedules.getType();
            this.startDate = schedules.getStartDate();
            this.endDate = schedules.getEndDate();
            this.allDay = schedules.isAllDay();
            this.customerId = schedules.getCustomerId();
            this.userId = schedules.getUserId();
            this.bigo = schedules.getBigo();
            this.payed = schedules.isPayed();
            this.tags = schedules.getTags();

            this.scheduleTypeName = this.scheduleType.getName();
            this.scheduleColor = this.scheduleType.getColor();
        }
    }

    @NoArgsConstructor @AllArgsConstructor
    @Getter @Setter
    public static class Post
    {
        private String name;
        private ScheduleType type;
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        private LocalDateTime startDate;
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        private LocalDateTime endDate;
        private boolean allDay = false;
        private Long customerId;
        private Long userId;
        private String bigo;
        private boolean payed = false;
        private Set<TagDto> tags = new HashSet<>();
    }

    @NoArgsConstructor @AllArgsConstructor
    @Getter @Setter
    public static class Put
    {
        private String name;
        private ScheduleType type;
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        private LocalDateTime startDate;
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        private LocalDateTime endDate;
        private boolean allDay;
        private Long customerId;
        private Long userId;
        private String bigo;
        private boolean payed;
        private Set<TagDto> tags = new HashSet<>();
    }

    @NoArgsConstructor @AllArgsConstructor
    @Getter @Setter
    public static class Get
    {
        private String name;
        private ScheduleType scheduleType;
        private String startDate;
        private String endDate;
        private Boolean allDay;
        private Long customerId;
        private Long userId;
        private String bigo;
        private Boolean payed;
        private String baseDate;
    }
}

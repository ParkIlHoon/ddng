package com.ddng.scheduleapi.schedule;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SCHEDULE")
public class Schedule
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private ScheduleType type;

    @Column(name = "START_DATE")
    private LocalDateTime startDate;

    @Column(name = "END_DATE")
    private LocalDateTime endDate;

    @Column(name = "IS_ALL_DAY")
    private boolean isAllDay;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "USER_ID")
    private Long userId;

    @Lob
    @Column(name = "BIGO")
    private String bigo;

    protected Schedule() { }
}

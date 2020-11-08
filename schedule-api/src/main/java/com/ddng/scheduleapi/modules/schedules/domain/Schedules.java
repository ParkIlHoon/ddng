package com.ddng.scheduleapi.modules.schedules.domain;

import com.ddng.scheduleapi.modules.tag.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SCHEDULES")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class Schedules
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

    @Column(name = "PAYED")
    private boolean payed;

    @Lob
    @Column(name = "BIGO")
    private String bigo;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Tag> tags = new HashSet<>();
}

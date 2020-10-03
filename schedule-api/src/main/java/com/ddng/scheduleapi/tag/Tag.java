package com.ddng.scheduleapi.tag;

import com.ddng.scheduleapi.schedule.Schedule;

import javax.persistence.*;

@Entity
@Table(name = "TAG")
public class Tag
{
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHEDULE_ID")
    private Schedule schedule;

    @Column(name = "TAG_ID")
    private Long tagId;

    protected Tag() { }
}

package com.ddng.scheduleapi.modules.tag;

import com.ddng.scheduleapi.modules.schedules.Schedules;
import javax.persistence.*;

@Entity
@Table(name = "TAG")
public class Tag
{
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SCHEDULE_ID")
    private Schedules schedules;

    @Column(name = "TAG_ID")
    private Long tagId;

    protected Tag() { }
}
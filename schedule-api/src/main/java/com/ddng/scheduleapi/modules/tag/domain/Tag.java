package com.ddng.scheduleapi.modules.tag.domain;

import com.ddng.scheduleapi.modules.schedules.domain.Schedules;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TAG")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class Tag
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Schedules> schedules = new HashSet<>();

    @Column(name = "TITLE")
    private String title;
}

package com.ddng.scheduleapi.modules.tag.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "TAG")
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class Tag
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE", unique = true, nullable = false)
    private String title;
}

package com.ddng.utils.tag;

import javax.persistence.*;

@Entity
@Table(name = "TAG")
public class Tag
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;
}

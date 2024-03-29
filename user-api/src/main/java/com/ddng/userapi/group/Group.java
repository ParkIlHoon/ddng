package com.ddng.userapi.group;

import javax.persistence.*;

@Entity
@Table(name = "USER_GROUP")
public class Group
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    protected Group() { }
}

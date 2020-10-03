package com.ddng.customerapi.family;

import javax.persistence.*;

@Entity
@Table(name = "FAMILY")
public class Family
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    protected Family() { }
}

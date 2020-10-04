package com.ddng.customerapi.modules.adopt.domain;

import com.ddng.customerapi.modules.customer.domain.Customer;
import com.ddng.customerapi.modules.family.domain.Family;

import javax.persistence.*;

@Entity
@Table(name = "ADOPT")
public class Adopt
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FAMILY_ID")
    private Family family;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    public Adopt() { }
}

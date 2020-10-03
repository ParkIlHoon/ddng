package com.ddng.customerapi.adopt;

import com.ddng.customerapi.customer.Customer;
import com.ddng.customerapi.family.Family;

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

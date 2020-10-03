package com.ddng.customerapi.tag;

import com.ddng.customerapi.customer.Customer;

import javax.persistence.*;

@Entity
@Table(name = "CUSTOMER_TAG")
public class Tag
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @Column(name = "TAG_ID")
    private Long tagId;

    protected Tag() { }
}

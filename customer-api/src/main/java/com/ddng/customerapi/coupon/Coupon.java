package com.ddng.customerapi.coupon;

import com.ddng.customerapi.customer.Customer;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "COUPON")
public class Coupon
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @Column(name = "EXPIRE_DATE")
    private LocalDateTime expireDate;

    @Column(name = "USE_DATE")
    private LocalDateTime useDate;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private CouponType type;

    @Column(name = "ITEM_ID")
    private Long itemId;

    protected Coupon() { }
}

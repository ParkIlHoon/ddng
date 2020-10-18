package com.ddng.saleapi.modules.coupon.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "COUPON")
public class Coupon
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

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

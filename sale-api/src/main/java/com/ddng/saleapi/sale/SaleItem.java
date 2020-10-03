package com.ddng.saleapi.sale;

import com.ddng.saleapi.coupon.Coupon;
import com.ddng.saleapi.item.Item;

import javax.persistence.*;

@Entity
@Table(name = "SALE_ITEM")
public class SaleItem
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SALE_ID")
    private Sale sale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @Column(name = "SALE_PRICE")
    private int salePrice;

    @Column(name = "COUNT")
    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COUPON_ID")
    private Coupon coupon;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    protected SaleItem() { }
}

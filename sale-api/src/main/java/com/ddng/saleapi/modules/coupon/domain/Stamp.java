package com.ddng.saleapi.modules.coupon.domain;

import com.ddng.saleapi.modules.item.domain.ItemType;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "STAMP")
@Getter
public class Stamp
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "ITEM_TYPE")
    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @Column(name = "COUNT")
    private int count;

    public Stamp(Long customerId, ItemType itemType)
    {
        this.customerId = customerId;
        this.itemType = itemType;
        this.count = 1;
    }

    public void add ()
    {
        this.count++;
    }
    public void remove (int removeCount)
    {
        this.count -= removeCount;
        if (this.count < 0)
        {
            this.count = 0;
        }
    }
}

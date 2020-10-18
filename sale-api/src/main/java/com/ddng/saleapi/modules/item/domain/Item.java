package com.ddng.saleapi.modules.item.domain;

import javax.persistence.*;

@Entity
@Table(name = "ITEM")
public class Item
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private ItemType type;

    @Column(name = "NAME")
    private String name;

    @Column(name = "BARCODE")
    private String barcode;

    @Column(name = "PRICE")
    private int price;

    @Column(name = "ITEM_QUANTITY")
    private int itemQuantity;

    protected Item() { }
}

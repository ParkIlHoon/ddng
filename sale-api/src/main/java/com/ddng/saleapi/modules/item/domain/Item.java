package com.ddng.saleapi.modules.item.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * <h1>상품 엔티티</h1>
 *
 * @version 1.0
 */
@Entity
@Table(name = "ITEM")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
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
}

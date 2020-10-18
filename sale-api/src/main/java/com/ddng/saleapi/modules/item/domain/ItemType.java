package com.ddng.saleapi.modules.item.domain;

import lombok.Getter;

/**
 * <h1>상품 타입 Enum 클래스</h1>
 *
 * @version 1.0
 */
@Getter
public enum ItemType
{
    ITEM("상품"),
    BEAUTY("미용"),
    HOTEL("호텔"),
    KINDERGARTEN("유치원");

    private String name;

    ItemType (String name)
    {
        this.name = name;
    }
}

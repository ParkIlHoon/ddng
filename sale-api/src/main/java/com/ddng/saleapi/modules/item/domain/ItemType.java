package com.ddng.saleapi.modules.item.domain;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>상품 타입 Enum 클래스</h1>
 *
 * @version 1.0
 */
@Getter
public enum ItemType
{
    SNACK("간식"),
    FEED("사료"),
    SUPPLIES("용품"),
    BEAUTY("미용"),
    HOTEL("호텔"),
    KINDERGARTEN("유치원");

    private String name;

    ItemType (String name)
    {
        this.name = name;
    }

    public static List<ItemType> findEnumByName(String name)
    {
        return Arrays.stream(ItemType.values())
                    .filter(itemType -> itemType.getName().trim().indexOf(name) > -1)
                    .collect(Collectors.toList());
    }
}

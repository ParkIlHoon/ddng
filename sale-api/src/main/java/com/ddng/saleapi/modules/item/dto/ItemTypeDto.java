package com.ddng.saleapi.modules.item.dto;

import com.ddng.saleapi.modules.item.domain.ItemType;
import lombok.*;

@Data
public class ItemTypeDto
{
    private String id;
    private String name;
    private String text;

    public ItemTypeDto(ItemType type)
    {
        this.id = type.name();
        this.name = type.getName();
        this.text = type.getName();
    }
}

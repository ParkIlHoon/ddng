package com.ddng.adminuibootstrap.modules.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ItemTypeDto
{
    private String id;
    private String name;
    private String text;

    public ItemTypeDto(String id, String name)
    {
        this.id = id;
        this.name = name;
        this.text = name;
    }
}

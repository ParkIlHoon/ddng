package com.ddng.adminuibootstrap.modules.common.dto.sale;

import lombok.*;

/**
 * <h1>ddng-sale-api 상품 타입 DTO</h1>
 */
@Data
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

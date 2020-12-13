package com.ddng.saleapi.modules.sale.dto;

import com.ddng.saleapi.modules.item.domain.ItemType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CalculateDto
{
    private ItemType itemType;
    private String itemTypeName;
    private int count;
    private int amount;

    @QueryProjection
    public CalculateDto(ItemType itemType, int count, int amount)
    {
        this.itemType = itemType;
        this.itemTypeName = itemType.getName();
        this.count = count;
        this.amount = amount;
    }
}

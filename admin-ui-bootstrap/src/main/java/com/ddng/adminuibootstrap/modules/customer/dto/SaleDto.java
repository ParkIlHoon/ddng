package com.ddng.adminuibootstrap.modules.customer.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaleDto
{
    private Long familyId;
    private String type;
    private String typeName;
    private LocalDateTime saleDate;
    private String paymentType;
    private String paymentTypeName;
    private List<SaleItemDto> saleItemList;

    public int getTotal()
    {
        int result = 0;
        for(SaleItemDto item : this.saleItemList)
        {
            result += item.getTotalPrice();
        }
        return result;
    }
}

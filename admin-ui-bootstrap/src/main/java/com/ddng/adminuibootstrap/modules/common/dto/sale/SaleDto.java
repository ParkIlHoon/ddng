package com.ddng.adminuibootstrap.modules.common.dto.sale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class SaleDto
{
    private Long id;
    private Long familyId;
    private SaleType type;
    private String typeName;
    private LocalDateTime saleDate;
    private PaymentType paymentType;
    private String paymentTypeName;
    private List<SaleItemDto> saleItemList = new ArrayList<>();

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

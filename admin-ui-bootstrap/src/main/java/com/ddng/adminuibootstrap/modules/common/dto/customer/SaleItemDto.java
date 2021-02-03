package com.ddng.adminuibootstrap.modules.common.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class SaleItemDto
{
    private Long itemId;
    private Long customerId;
    private String itemName;
    private int count;
    private int salePrice;
    private int totalPrice;
    private Long scheduleId;
    private String couponName;
}

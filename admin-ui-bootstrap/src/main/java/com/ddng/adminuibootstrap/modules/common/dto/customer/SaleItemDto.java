package com.ddng.adminuibootstrap.modules.common.dto.customer;

import lombok.Data;

@Data
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

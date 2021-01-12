package com.ddng.adminuibootstrap.modules.common.dto.sale;

import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <h1>ddng-sale-api 판매 상품 DTO</h1>
 */
@Data @Builder
public class SaleItemDto
{
    @NotNull
    private Long itemId;
    @NotNull
    private int count;
    private Long couponId;
    private Long customerId;
    private Long scheduleId;
}

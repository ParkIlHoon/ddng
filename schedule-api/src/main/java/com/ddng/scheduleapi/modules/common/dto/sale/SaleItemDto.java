package com.ddng.scheduleapi.modules.common.dto.sale;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>ddng-sale-api 판매 상품 DTO</h1>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleItemDto {
    @NotNull
    private Long itemId;
    @NotNull
    private int count;
    private Long couponId;
    private Long customerId;
    private Long scheduleId;
    private String itemName;
    private int salePrice;
    private int totalPrice;
    private String couponName;
}

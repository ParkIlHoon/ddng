package com.ddng.statisticapi.modules.sale.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
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

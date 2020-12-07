package com.ddng.adminuibootstrap.modules.sale.vo;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = {"itemId", "customerId", "scheduleId", "couponId"})
public class CartItem
{
    private Long itemId;
    private int count;
    private int salePrice;
    private Long customerId;
    private Long couponId;
    private Long scheduleId;
}

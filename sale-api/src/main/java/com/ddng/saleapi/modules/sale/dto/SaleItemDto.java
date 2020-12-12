package com.ddng.saleapi.modules.sale.dto;

import com.ddng.saleapi.modules.sale.domain.SaleItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data @Builder
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

    @Data @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Get
    {
        private Long itemId;
        private Long customerId;
        private String itemName;
        private int count;
        private int salePrice;
        private int totalPrice;
        private Long scheduleId;
        private String couponName;

        public Get (SaleItem saleItem)
        {
            this.itemId = saleItem.getItem().getId();
            this.customerId = saleItem.getCustomerId();
            this.itemName = saleItem.getItem().getName();
            this.count = saleItem.getCount();
            this.salePrice = saleItem.getSalePrice();
            this.totalPrice = saleItem.getTotalPrice();
            this.scheduleId = saleItem.getScheduleId();
            if(saleItem.getCoupon() != null)
            {
                this.couponName = saleItem.getCoupon().getName();
            }
        }
    }
}

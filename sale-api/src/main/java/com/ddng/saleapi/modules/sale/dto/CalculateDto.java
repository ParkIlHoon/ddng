package com.ddng.saleapi.modules.sale.dto;

import com.ddng.saleapi.modules.item.domain.ItemType;
import com.ddng.saleapi.modules.sale.domain.PaymentType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class CalculateDto
{
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class ByItem
    {
        private ItemType itemType;
        private String itemTypeName;
        private int count;
        private int amount;

        @QueryProjection
        public ByItem(ItemType itemType, int count, int amount)
        {
            this.itemType = itemType;
            this.itemTypeName = itemType.getName();
            this.count = count;
            this.amount = amount;
        }
    }

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    public static class ByPayment
    {
        private PaymentType paymentType;
        private String paymentTypeName;
        private int count;
        private int amount;

        @QueryProjection
        public ByPayment(PaymentType paymentType, int count, int amount)
        {
            this.paymentType = paymentType;
            this.paymentTypeName = paymentType.getName();
            this.count = count;
            this.amount = amount;
        }
    }
}

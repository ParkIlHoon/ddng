package com.ddng.adminuibootstrap.modules.common.dto.statistic;

import lombok.*;

/**
 * <h1>판매 정산 DTO</h1>
 */
public class CalculateDto
{
    /**
     * <h2>상품 분류별 정산 DTO</h2>
     */
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @ToString
    public static class ByItemType
    {
        private String itemType;
        private String itemTypeName;
        private int count;
        private int amount;
    }

    /**
     * <h2>결제 수단별 정산 DTO</h2>
     */
    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @ToString
    public static class ByPayment
    {
        private String paymentType;
        private String paymentTypeName;
        private int count;
        private int amount;
    }
}

package com.ddng.saleapi.modules.sale.domain;

import lombok.Getter;

/**
 * <h1>결재 수단 Enum 클래스</h1>
 *
 * @version 1.0
 */
@Getter
public enum PaymentType
{
    CASH("현금"),
    CARD("카드"),
    COUPON("쿠폰");

    private String name;

    PaymentType(String name)
    {
        this.name = name;
    }
}

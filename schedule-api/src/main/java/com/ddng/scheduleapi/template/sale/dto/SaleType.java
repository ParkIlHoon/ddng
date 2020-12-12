package com.ddng.scheduleapi.template.sale.dto;

import lombok.Getter;

/**
 * <h1>판매 타입 Enum 클래스</h1>
 *
 * @version 1.0
 */
@Getter
public enum SaleType
{
    ORDER("주문"),
    PAYED("결재"),
    CANCEL("환불"),
    CREDIT("외상");

    private String name;

    SaleType(String name)
    {
        this.name = name;
    }
}

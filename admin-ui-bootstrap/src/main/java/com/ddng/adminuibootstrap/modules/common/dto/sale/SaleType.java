package com.ddng.adminuibootstrap.modules.common.dto.sale;

import lombok.Getter;

/**
 * <h1>ddng-sale-api 판매 타입 Enum 클래스</h1>
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

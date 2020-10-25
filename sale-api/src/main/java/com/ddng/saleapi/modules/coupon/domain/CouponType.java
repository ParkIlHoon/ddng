package com.ddng.saleapi.modules.coupon.domain;

import java.util.function.Function;

/**
 * <h1>쿠폰 타입 Enum 클래스</h1>
 *
 * 쿠폰 타입에 따라 할인된 금액을 calculate 메서드로 취득할 수 있다.
 *
 * @version 1.0
 */
public enum CouponType
{
    DISCOUNT_ALL("무료", value -> 0),
    DISCOUNT_HALF("반값할인", value -> value / 2);

    private String name;
    private Function<Integer, Integer> expression;

    CouponType(String name, Function<Integer, Integer> expression)
    {
        this.name = name;
        this.expression = expression;
    }

    public String getName ()
    {
        return this.name;
    }

    public int calculate (int value)
    {
        return this.expression.apply(value);
    }
}

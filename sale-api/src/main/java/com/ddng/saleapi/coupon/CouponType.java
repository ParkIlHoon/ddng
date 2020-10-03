package com.ddng.saleapi.coupon;

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
    DISCOUNT_ALL(value -> 0),
    DISCOUNT_HALF(value -> value / 2);

    private Function<Integer, Integer> expression;

    CouponType(Function<Integer, Integer> expression) {
        this.expression = expression;
    }

    public int calculate (int value)
    {
        return this.expression.apply(value);
    }
}

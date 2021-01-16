package com.ddng.saleapi.modules.coupon.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <h1>쿠폰 발행 이벤트</h1>
 *
 */
@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class NewCouponEvent implements Serializable
{
    /**
     * 판매 아이디
     */
    private final Long saleId;
    /**
     * 고객 아이디
     */
    private final List<Long> customerId;
}

package com.ddng.saleapi.modules.coupon.repository;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface StampCustomRepository
{
    /**
     * 쿠폰 발급 가능한 사용자 아이디 목록 조회
     * @param standardCount 쿠폰 발급 기준 개수
     * @return
     */
    List<Long> getCouponIssuableCustomerIds(int standardCount);
}

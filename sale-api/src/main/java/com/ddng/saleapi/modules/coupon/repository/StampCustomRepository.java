package com.ddng.saleapi.modules.coupon.repository;

import com.ddng.saleapi.modules.coupon.domain.Stamp;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface StampCustomRepository
{
    /**
     * 쿠폰 발급 가능한 사용자 아이디 목록 조회
     * @param customerIds 검색 대상 사용자 아이디 목록
     * @param standardCount 쿠폰 발급 기준 개수
     * @return
     */
    public List<Long> getCouponChangableCustomerIds(List<Long> customerIds, int standardCount);
}

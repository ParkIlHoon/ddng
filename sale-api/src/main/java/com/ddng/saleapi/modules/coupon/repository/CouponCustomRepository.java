package com.ddng.saleapi.modules.coupon.repository;

import com.ddng.saleapi.modules.coupon.dto.CouponDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CouponCustomRepository
{
    Page<CouponDto.Response> searchCoupon (CouponDto.Get dto, Pageable pageable);
}

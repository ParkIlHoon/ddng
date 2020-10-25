package com.ddng.saleapi.modules.coupon.service;

import com.ddng.saleapi.modules.coupon.domain.Coupon;
import com.ddng.saleapi.modules.coupon.dto.CouponDto;
import com.ddng.saleapi.modules.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponService
{
    private final CouponRepository couponRepository;

    public Page<CouponDto.Response> searchByDto(CouponDto.Get dto, Pageable pageable)
    {
        return couponRepository.searchCoupon(dto, pageable);
    }

    public Optional<Coupon> findById(Long id)
    {
        return couponRepository.findById(id);
    }
}

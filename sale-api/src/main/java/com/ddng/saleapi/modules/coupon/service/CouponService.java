package com.ddng.saleapi.modules.coupon.service;

import com.ddng.saleapi.modules.coupon.domain.Coupon;
import com.ddng.saleapi.modules.coupon.domain.CouponType;
import com.ddng.saleapi.modules.coupon.domain.Stamp;
import com.ddng.saleapi.modules.coupon.dto.CouponDto;
import com.ddng.saleapi.modules.coupon.repository.CouponRepository;
import com.ddng.saleapi.modules.coupon.repository.StampRepository;
import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.sale.domain.Sale;
import com.ddng.saleapi.modules.sale.domain.SaleItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <h1>쿠폰 서비스</h1>
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CouponService
{
    private final CouponRepository couponRepository;
    private final StampRepository stampRepository;

    /**
     * <h2>쿠폰 목록 조회</h2>
     * @param dto
     * @param pageable
     * @return
     */
    public Page<CouponDto.Response> searchByDto(CouponDto.Get dto, Pageable pageable)
    {
        return couponRepository.searchCoupon(dto, pageable);
    }

    /**
     * <h2>쿠폰 조회</h2>
     * @param id
     * @return
     */
    public Optional<Coupon> findById(Long id)
    {
        return couponRepository.findById(id);
    }

    /**
     * <h2>스탬프 적립</h2>
     * @param sale 스탬프를 적립하는 판매 객체
     */
    public void stamp(Sale sale)
    {
        /**
         * 1. 구매 상품 중 스탬프 적립 가능한 상품 목록 추출
         * 2. 스탬프 적립 가능한 상품 개수만큼 스탬프 객체 생성
         * 3. 생성한 스탬프 객체 저장
         */
        // 스탬프 적립 가능 상품 필터링 및 스탬프 객체 생성
        List<Stamp> stamps = sale.getSaleItemList().stream()
                                    .filter(SaleItem::isEnableToStamp)
                                    .map(Stamp::new)
                                    .collect(Collectors.toList());

        // 스탬프 객체 저장
        List<Stamp> savedStamps = stampRepository.saveAll(stamps);
    }

    /**
     * <h2>쿠폰 발급 가능한 사용자 아이디 조회</h2>
     * @return
     */
    public List<Long> getCouponIssuableCustomerIds()
    {
        return stampRepository.getCouponIssuableCustomerIds(11);
    }


    /**
     * <h2>쿠폰 생성</h2>
     * 스탬프 적립 시 쿠폰 발급 가능한 개수에 도달했을 경우, 본 메서드로 쿠폰을 생성해 발급한다.
     * @param customerId
     * @param item
     * @param type
     */
    private void createCoupon(Long customerId, Item item, CouponType type)
    {
        Coupon newCoupon = new Coupon();
        newCoupon.setType(type);
        newCoupon.setItem(item);
        newCoupon.setCustomerId(customerId);
        newCoupon.setCreateDate(LocalDateTime.now());
        newCoupon.setExpireDate(LocalDateTime.now().plusYears(1));
        newCoupon.setName(item.getName() + " " + type.getName() + " 쿠폰");

        couponRepository.save(newCoupon);
    }
}

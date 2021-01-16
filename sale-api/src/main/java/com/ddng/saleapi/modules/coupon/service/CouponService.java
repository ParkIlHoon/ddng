package com.ddng.saleapi.modules.coupon.service;

import com.ddng.saleapi.modules.coupon.domain.Coupon;
import com.ddng.saleapi.modules.coupon.domain.CouponType;
import com.ddng.saleapi.modules.coupon.domain.Stamp;
import com.ddng.saleapi.modules.coupon.dto.CouponDto;
import com.ddng.saleapi.modules.coupon.event.NewCouponEvent;
import com.ddng.saleapi.modules.coupon.repository.CouponRepository;
import com.ddng.saleapi.modules.coupon.repository.StampRepository;
import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.item.domain.ItemType;
import com.ddng.saleapi.modules.sale.domain.Sale;
import com.ddng.saleapi.modules.sale.domain.SaleItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
     * <ol>
     *     <li>구매 고객에게 스탬프를 적립</li>
     *     <li>적립할 스탬프 개수와 기존 스탬프 개수의 합이 쿠폰 발급 기준 개수를 만족할 경우, 쿠폰 발급 이벤트 객체 반환</li>
     * </ol>
     * @param sale 스탬프를 적립하는 판매 객체
     * @return 쿠폰 발급 이벤트 객체
     */
    public NewCouponEvent stamp(Sale sale)
    {
        /**
         * 1. 구매 상품 중 스탬프 적립 가능한 상품 목록 추출
         * 2. 스탬프 적립 가능한 상품 개수만큼 스탬프 객체 생성
         * 3. 생성한 스탬프 객체 저장
         * 4. 전체 스탬프 개수가 쿠폰 발급 기준 개수를 만족할 경우 쿠폰 발급 이벤트 객체 생성 및 반환
         */
        // 스탬프 적립 가능 상품 필터링 및 스탬프 객체 생성
        List<Stamp> stamps = sale.getSaleItemList().stream()
                                    .filter(SaleItem::isEnableToStamp)
                                    .map(Stamp::new)
                                    .collect(Collectors.toList());

        // 스탬프 객체 저장
        List<Stamp> savedStamps = stampRepository.saveAll(stamps);

        // 쿠폰 발급 가능한 사용자 조회
        List<Long> customerIds = savedStamps.stream().map(Stamp::getCustomerId).distinct().collect(Collectors.toList());
        List<Long> couponChangableCustomerIds = stampRepository.getCouponChangableCustomerIds(customerIds, 11);

        // 쿠폰 발급 이벤트 객체 생성
        NewCouponEvent event = new NewCouponEvent(sale.getId(), couponChangableCustomerIds);

        return event;
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

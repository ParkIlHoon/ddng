package com.ddng.saleapi.modules.coupon.service;

import com.ddng.saleapi.modules.common.dto.CustomerDto;
import com.ddng.saleapi.modules.common.template.CustomerTemplate;
import com.ddng.saleapi.modules.coupon.domain.Coupon;
import com.ddng.saleapi.modules.coupon.domain.Stamp;
import com.ddng.saleapi.modules.coupon.dto.CouponDto;
import com.ddng.saleapi.modules.coupon.repository.CouponRepository;
import com.ddng.saleapi.modules.coupon.repository.StampRepository;
import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.item.service.ItemService;
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
    private final ItemService itemService;
    private final CustomerTemplate customerTemplate;

    private static final int COUNT_OF_ISSUE_COUPON = 11;

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
        return stampRepository.getCouponIssuableCustomerIds(COUNT_OF_ISSUE_COUPON);
    }

    /**
     * <h2>신규 쿠폰 생성</h2>
     * @param dto 신규 생성할 쿠폰 정보
     * @return
     */
    public Coupon issueNewCoupon(CouponDto.Post dto)
    {
        // 고객 조회
        Optional<CustomerDto> customerDto = customerTemplate.getCustomer(dto.getCustomerId());
        if(customerDto.isEmpty())
        {
            //TODO 고객 미존재
            throw new IllegalArgumentException("존재하지 않는 고객입니다.");
        }

        // 상품 조회
        Optional<Item> optionalItem = itemService.findById(dto.getItemId());
        if(optionalItem.isEmpty())
        {
            //TODO 상품 미존재 시 Exception throws 시키자
            throw new IllegalArgumentException("존재하지 않는 상품입니다.");
        }

        // 저장 처리
        Coupon coupon = Coupon.builder()
                                .customerId(customerDto.get().getId())
                                .item(optionalItem.get())
                                .type(dto.getType())
                                .name(optionalItem.get().getName() + " " + dto.getType().getName() + " 쿠폰")
                                .createDate(LocalDateTime.now())
                                .expireDate(LocalDateTime.now().plusYears(1))
                                .build();

        Coupon save = couponRepository.save(coupon);

        // Stamp 사용 처리
        List<Stamp> stampsForIssueCoupon = stampRepository.getStampsForIssueCoupon(dto.getCustomerId(), COUNT_OF_ISSUE_COUPON);
        stampsForIssueCoupon.stream().forEach(stamp -> stamp.setCoupon(coupon));
        stampRepository.saveAll(stampsForIssueCoupon);

        return save;
    }
}

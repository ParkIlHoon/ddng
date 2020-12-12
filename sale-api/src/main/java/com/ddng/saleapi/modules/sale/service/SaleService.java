package com.ddng.saleapi.modules.sale.service;

import com.ddng.saleapi.modules.coupon.domain.Coupon;
import com.ddng.saleapi.modules.coupon.repository.CouponRepository;
import com.ddng.saleapi.modules.coupon.service.CouponService;
import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.item.repository.ItemRepository;
import com.ddng.saleapi.modules.sale.domain.Sale;
import com.ddng.saleapi.modules.sale.domain.SaleItem;
import com.ddng.saleapi.modules.sale.dto.SaleDto;
import com.ddng.saleapi.modules.sale.dto.SaleItemDto;
import com.ddng.saleapi.modules.sale.event.EventDispatcher;
import com.ddng.saleapi.modules.sale.event.SellingEvent;
import com.ddng.saleapi.modules.sale.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <h1>판매 서비스 컨트롤러</h1>
 *
 * @version 1.0
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SaleService
{
    private final SaleRepository saleRepository;
    private final ItemRepository itemRepository;
    private final CouponRepository couponRepository;
    private final CouponService couponService;
    private final EventDispatcher eventDispatcher;
    private final EntityManager entityManager;

    /**
     * 새로운 판매를 생성한다.
     * @param dto
     * @return
     */
    public Sale createSale(SaleDto.Post dto)
    {
        // 판매할 상품 확인 및 조회
        List<SaleItemDto> saleItemDtos = dto.getSaleItems();
        List<Long> itemIds = saleItemDtos.stream()
                                            .map(saleItemDto -> saleItemDto.getItemId())
                                            .collect(Collectors.toList());
        List<Item> items = itemRepository.findByIdIn(itemIds);

        // 사용 가능한 쿠폰 확인
        List<Long> couponIds = dto.getSaleItems().stream()
                                                    .filter(saleItemDto -> saleItemDto.getCouponId() != null)
                                                    .map(saleItemDto -> saleItemDto.getCouponId())
                                                    .collect(Collectors.toList());
        List<Coupon> coupons = couponRepository.findByIdInAndUseDateIsNullAndExpireDateAfter(couponIds, LocalDateTime.now());

        // 판매 생성
        Sale sale = new Sale(dto);

        // 판매 저장
        Sale save = saleRepository.save(sale);

        // 판매 상품 생성
        List<SaleItem> saleItems = createSaleItems(saleItemDtos, items, coupons);
        for (SaleItem saleItem : saleItems)
        {
            save.addSaleItem(saleItem);
        }

        //TODO 쿠폰 처리
        //couponService.stamp(save);

        entityManager.flush();

        // 이벤트 발행
        eventDispatcher.send(new SellingEvent(sale.getId()));

        return save;
    }

    /**
     * 판매 상품 객체 리스트를 생성해 반환한다.
     * @param dtos 판매 정보 객체 리스트
     * @param items 판매 상품 객체 리스트
     * @param coupons 적용 쿠폰 객체 리스트
     * @return
     */
    private List<SaleItem> createSaleItems (List<SaleItemDto> dtos, List<Item> items, List<Coupon> coupons)
    {
        List<SaleItem> returnList = new ArrayList<>();
        for (SaleItemDto dto : dtos)
        {
            Optional<Item> optionalItem = items.stream().filter(item -> item.getId().equals(dto.getItemId())).findFirst();

            if (optionalItem.isPresent())
            {
                SaleItem saleItem = new SaleItem(dto, optionalItem.get());

                // 상품 재고 차감
                optionalItem.get().soldItem(dto.getCount());

                if (dto.getCouponId() != null)
                {
                    Optional<Coupon> optionalCoupon = coupons.stream()
                                                                .filter(coupon -> coupon.isUsable(optionalItem.get(), dto.getCustomerId()))
                                                                .findFirst();

                    if (optionalCoupon.isPresent())
                    {
                        // 쿠폰 적용
                        saleItem.applyCoupon(optionalCoupon.get());
                    }
                }

                returnList.add(saleItem);
            }
        }

        return returnList;
    }

    public Optional<Sale> findById(Long id)
    {
        return saleRepository.findById(id);
    }

    public Page<SaleDto.Response> searchByDto(SaleDto.Get dto, Pageable pageable)
    {
        Page<Sale> salePage = null;
        if (dto.isOnlyToday() && dto.getItem() == null)
        {
            salePage = saleRepository.findBySaleDateAfterAndSaleDateBefore(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), pageable);
        }

        if (!dto.isOnlyToday() && dto.getItem() != null)
        {
            salePage = saleRepository.findSaleByItem(dto.getItem(), pageable);
        }

        List<SaleDto.Response> collect = salePage.getContent().stream()
                                                    .map(sale -> new SaleDto.Response(sale))
                                                    .collect(Collectors.toList());
        PageImpl<SaleDto.Response> responses = new PageImpl<SaleDto.Response>(collect, pageable, salePage.getTotalElements());
        return responses;
    }

    public Sale updateSale(Sale sale, SaleDto.Put dto)
    {
        sale.setType(dto.getType());
        return saleRepository.save(sale);
    }
}

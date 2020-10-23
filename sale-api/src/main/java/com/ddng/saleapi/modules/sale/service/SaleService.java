package com.ddng.saleapi.modules.sale.service;

import com.ddng.saleapi.modules.coupon.domain.Coupon;
import com.ddng.saleapi.modules.coupon.repository.CouponRepository;
import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.item.repository.ItemRepository;
import com.ddng.saleapi.modules.sale.domain.Sale;
import com.ddng.saleapi.modules.sale.domain.SaleItem;
import com.ddng.saleapi.modules.sale.dto.SaleDto;
import com.ddng.saleapi.modules.sale.dto.SaleItemDto;
import com.ddng.saleapi.modules.sale.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        // 판매 상품 생성
        List<SaleItem> saleItems = createSaleItems(saleItemDtos, items, coupons);
        sale.setSaleItemList(saleItems);

        // 판매 저장(cascade 처리)
        Sale save = saleRepository.save(sale);

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
}

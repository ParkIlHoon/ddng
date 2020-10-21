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
        List<Coupon> coupons = couponRepository.findByIdIn(couponIds);

        // 판매 생성
        Sale sale = new Sale();
        sale.setFamilyId(dto.getFamilyId());
        sale.setPayment(dto.getPayment());
        sale.setSaleDate(LocalDateTime.now());
        sale.setType(dto.getType());

        // 판매 상품 생성
        for (SaleItemDto test : saleItemDtos)
        {
            Optional<Item> first = items.stream().filter(item -> item.getId().equals(test.getItemId())).findFirst();
            if (!first.isEmpty())
            {
                SaleItem saleItem = new SaleItem();
                saleItem.setItem(first.get());
                saleItem.setCount(test.getCount());
                saleItem.setSalePrice(first.get().getPrice());
                saleItem.setCustomerId(test.getCustomerId());

                if (test.getCouponId() != null)
                {
                    Optional<Coupon> optionalCoupon = coupons.stream()
                                                                .filter(coupon -> coupon.getCustomerId().equals(test.getCouponId()))
                                                            .findFirst();

                    if (optionalCoupon.isPresent())
                    {
                        Coupon coupon = optionalCoupon.get();
                        saleItem.setCoupon(coupon);
                        coupon.setUseDate(LocalDateTime.now());
                    }
                }

                sale.getSaleItemList().add(saleItem);
            }
        }

        // 판매 저장(cascade 처리)
        Sale save = saleRepository.save(sale);

        return save;
    }
}

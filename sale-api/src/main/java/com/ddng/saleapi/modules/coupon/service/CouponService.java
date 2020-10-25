package com.ddng.saleapi.modules.coupon.service;

import com.ddng.saleapi.modules.coupon.domain.Coupon;
import com.ddng.saleapi.modules.coupon.domain.CouponType;
import com.ddng.saleapi.modules.coupon.domain.Stamp;
import com.ddng.saleapi.modules.coupon.dto.CouponDto;
import com.ddng.saleapi.modules.coupon.repository.CouponRepository;
import com.ddng.saleapi.modules.coupon.repository.StampRepository;
import com.ddng.saleapi.modules.item.domain.ItemType;
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

@Service
@Transactional
@RequiredArgsConstructor
public class CouponService
{
    private final CouponRepository couponRepository;
    private final StampRepository stampRepository;

    public Page<CouponDto.Response> searchByDto(CouponDto.Get dto, Pageable pageable)
    {
        return couponRepository.searchCoupon(dto, pageable);
    }

    public Optional<Coupon> findById(Long id)
    {
        return couponRepository.findById(id);
    }

    public void stamp(Sale sale)
    {
        List<SaleItem> saleItems = sale.getSaleItemList();
        List<SaleItem> stampSaleItems = saleItems.stream()
                                                    .filter(saleItem -> saleItem.getCoupon() == null && saleItem.getItem().isStamp())
                                                    .collect(Collectors.toList());

        for (SaleItem saleItem : saleItems)
        {
            Long customerId = saleItem.getCustomerId();
            Optional<Stamp> optionalStamp = stampRepository.findByCustomerIdAndItemType(customerId, saleItem.getItem().getType());

            if(optionalStamp.isPresent())
            {
                optionalStamp.get().add();
            }
            else
            {
                Stamp stamp = new Stamp(customerId, saleItem.getItem().getType());
                stampRepository.save(stamp);
            }

            optionalStamp = stampRepository.findByCustomerIdAndItemType(customerId, saleItem.getItem().getType());
            if (optionalStamp.get().getCount() >= 11)
            {
                createCoupon(customerId, saleItem.getItem().getType(), CouponType.DISCOUNT_ALL);
                optionalStamp.get().remove(11);
            }
        }
    }

    private void createCoupon(Long customerId, ItemType itemType, CouponType type)
    {
        Coupon newCoupon = new Coupon();
        newCoupon.setItemType(itemType);
        newCoupon.setType(type);
        newCoupon.setCustomerId(customerId);
        newCoupon.setCreateDate(LocalDateTime.now());
        newCoupon.setExpireDate(LocalDateTime.now().plusYears(1));
        newCoupon.setName(itemType.getName() + " " + type.getName() + " 쿠폰");

        couponRepository.save(newCoupon);
    }

}

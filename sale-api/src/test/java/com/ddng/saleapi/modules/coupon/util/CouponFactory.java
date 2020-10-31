package com.ddng.saleapi.modules.coupon.util;

import com.ddng.saleapi.modules.coupon.domain.Coupon;
import com.ddng.saleapi.modules.coupon.domain.CouponType;
import com.ddng.saleapi.modules.coupon.repository.CouponRepository;
import com.ddng.saleapi.modules.item.domain.ItemType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <h1>쿠폰 생성 팩토리</h1>
 */
@Component
@Transactional
@RequiredArgsConstructor
public class CouponFactory
{
    private final CouponRepository couponRepository;

    /**
     * 쿠폰을 생성한다
     * @param name
     * @param type
     * @param itemType
     * @param createDate
     * @param expireDate
     * @param useDate
     * @return
     */
    public Coupon createCoupon (String name, CouponType type, ItemType itemType, LocalDateTime createDate, LocalDateTime expireDate, LocalDateTime useDate)
    {
        Coupon coupon = new Coupon();
        coupon.setName(name);
        coupon.setType(type);
        coupon.setItemType(itemType);
        coupon.setCreateDate(createDate);
        coupon.setExpireDate(expireDate);
        coupon.setUseDate(useDate);

        return couponRepository.save(coupon);
    }
}

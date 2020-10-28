package com.ddng.saleapi.modules.coupon.dto;

import com.ddng.saleapi.modules.coupon.domain.Coupon;
import com.ddng.saleapi.modules.coupon.domain.CouponType;
import com.ddng.saleapi.modules.item.domain.ItemType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class CouponDto
{
    @Getter @Setter @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Get
    {
        private List<Long> customerIds;
    }

    @Getter @Setter @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Response
    {
        private Long id;
        private Long customerId;
        private String name;
        private LocalDateTime createDate;
        private LocalDateTime expireDate;
        private LocalDateTime useDate;
        private CouponType type;
        private String typeName;
        private ItemType itemType;
        private String itemTypeName;

        public Response(Coupon coupon)
        {
            this.id = coupon.getId();
            this.customerId = coupon.getCustomerId();
            this.name = coupon.getName();
            this.createDate = coupon.getCreateDate();
            this.expireDate = coupon.getExpireDate();
            this.useDate = coupon.getUseDate();
            this.type = coupon.getType();
            this.typeName = coupon.getType().getName();
        }

        @QueryProjection
        public Response(Long id, Long customerId, String name,
                        LocalDateTime createDate, LocalDateTime expireDate, LocalDateTime useDate,
                        CouponType type, ItemType itemType)
        {
            this.id = id;
            this.customerId = customerId;
            this.name = name;
            this.createDate = createDate;
            this.expireDate = expireDate;
            this.useDate = useDate;
            this.type = type;
            this.typeName = this.type.getName();
            this.itemType = itemType;
            this.itemTypeName = this.itemType.getName();
        }
    }
}

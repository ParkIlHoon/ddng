package com.ddng.saleapi.modules.sale.domain;

import com.ddng.saleapi.modules.coupon.domain.Coupon;
import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.sale.dto.SaleItemDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SALE_ITEM")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class SaleItem
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SALE_ID")
    private Sale sale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @Column(name = "SALE_PRICE")
    private int salePrice;

    @Column(name = "COUNT")
    private int count;

    @Column(name = "TOTAL_PRICE")
    private int totalPrice;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "COUPON_ID")
    private Coupon coupon;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "SCHEDULE_ID")
    private Long scheduleId;

    public SaleItem(SaleItemDto dto, Item item)
    {
        this.item = item;
        this.salePrice = item.getPrice();
        this.count = dto.getCount();
        this.customerId = dto.getCustomerId();
        this.totalPrice = this.salePrice * this.count;
        this.scheduleId = dto.getScheduleId();
    }

    /**
     * 쿠폰을 적용한다.
     * @param coupon 적용할 쿠폰
     */
    public void applyCoupon (Coupon coupon)
    {
        this.coupon = coupon;
        this.totalPrice = this.salePrice * (this.count - 1) + coupon.getType().calculate(this.salePrice);
        coupon.setUseDate(LocalDateTime.now());
    }

    /**
     * 스탬프 적립 가능 여부를 반환한다.
     * @return 스탬프 적립 가능 여부
     */
    public boolean isEnableToStamp()
    {
        return this.coupon == null && this.item.isStamp();
    }

    /**
     * 스탬프 적립 개수를 반환한다.
     * @return 적립할 스탬프 개수
     */
    public int getStampCount()
    {
        return this.count;
    }
}

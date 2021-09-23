package com.ddng.saleapi.modules.coupon.domain;

import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.sale.domain.Sale;
import com.ddng.saleapi.modules.sale.domain.SaleItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@ApiModel(value = "스탬프 엔티티")
@Entity
@Table(name = "STAMP")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Stamp
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    @ApiModelProperty(value = "스탬프 아이디", dataType = "Long", required = true, example = "1")
    private Long id;

    @Column(name = "CUSTOMER_ID", unique = true, nullable = false)
    @ApiModelProperty(value = "고객 아이디", dataType = "Long", required = true, example = "1")
    private Long customerId;

    @Column(name = "STAMP_DATE", nullable = false)
    @ApiModelProperty(value = "스탬프 적립 일시", dataType = "LocalDateTime")
    private LocalDateTime stampDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SALE_ID")
    @ApiModelProperty(value = "스탬프 적립 판매 아이디", notes = "스탬프를 적립한 판매의 아이디")
    private Sale sale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    @ApiModelProperty(value = "스탬프 적립 상품 아이디", notes = "스탬프를 적립한 상품의 아이디")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COUPON_ID")
    @ApiModelProperty(value = "쿠폰 아이디", notes = "쿠폰 발급 개수를 만족해 전환된 쿠폰의 아이디")
    private Coupon coupon;

    /**
     * 스탬프가 쿠폰으로 전환되었는지 여부 반환
     * @return 스탬프가 쿠폰으로 전환되었는지 여부
     */
    public boolean isChangedToCoupon()
    {
        return coupon != null;
    }

    public Stamp(SaleItem saleItem)
    {
        this.customerId = saleItem.getCustomerId();
        this.sale = saleItem.getSale();
        this.item = saleItem.getItem();
    }
}

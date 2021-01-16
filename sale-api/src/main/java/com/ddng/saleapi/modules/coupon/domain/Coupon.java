package com.ddng.saleapi.modules.coupon.domain;

import com.ddng.saleapi.modules.item.domain.Item;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@ApiModel(value = "쿠폰 엔티티")
@Entity
@Table(name = "COUPON")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Coupon
{
    @Id @GeneratedValue
    @Column(name = "ID")
    @ApiModelProperty(value = "쿠폰 아이디", dataType = "Long", example = "1")
    private Long id;

    @Column(name = "CUSTOMER_ID", nullable = false)
    @ApiModelProperty(value = "고객 아이디", dataType = "Long", example = "1", required = true)
    private Long customerId;

    @Column(name = "NAME")
    @ApiModelProperty(value = "쿠폰 이름", dataType = "String", example = "2021 고객 감사 쿠폰")
    private String name;

    @Column(name = "CREATE_DATE")
    @ApiModelProperty(value = "쿠폰 생성일시", dataType = "LocalDateTime")
    private LocalDateTime createDate;

    @Column(name = "EXPIRE_DATE")
    @ApiModelProperty(value = "쿠폰 만료일시", dataType = "LocalDateTime")
    private LocalDateTime expireDate;

    @Column(name = "USE_DATE")
    @ApiModelProperty(value = "쿠폰 사용일시", dataType = "LocalDateTime")
    private LocalDateTime useDate;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "쿠폰 타입", dataType = "CouponType", example = "DISCOUNT_ALL")
    private CouponType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    @ApiModelProperty(value = "쿠폰 적용 상품", dataType = "Long", example = "1")
    private Item item;

    /**
     * 쿠폰을 사용할 수 있는지 확인한다.
     * @param item 사용할 상품
     * @param customerId 적용할 대상 고객 아이디
     * @return 쿠폰 사용 가능 여부
     *
     */
    public boolean isUsable (Item item, Long customerId)
    {
        if (useDate != null) return false;
        if (expireDate.isBefore(LocalDateTime.now())) return false;

        return this.item.getId().equals(item.getId());
    }
}

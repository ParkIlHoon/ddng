package com.ddng.saleapi.modules.coupon.domain;

import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.item.domain.ItemType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "COUPON")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Coupon
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @Column(name = "EXPIRE_DATE")
    private LocalDateTime expireDate;

    @Column(name = "USE_DATE")
    private LocalDateTime useDate;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private CouponType type;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ITEM_ID")
    private Item item;


    /**
     * 쿠폰을 사용할 수 있는지 확인한다.
     * @param item 사용할 상품
     * @param customerId 적용할 대상 고객 아이디
     *                   <ul>
     *                       <li>상품이 미용, 호텔, 유치원일 경우 : 대상자 아이디 필수</li>
     *                       <li>상품이 간식, 사료, 용품일 경우 : 대상자 아이디 선택</li>
     *                   </ul>
     * @return 쿠폰 사용 가능 여부
     *
     */
    public boolean isUsable (Item item, Long customerId)
    {
        if (useDate != null) return false;
        if (expireDate.isBefore(LocalDateTime.now())) return false;

        // 상품이 미용, 호텔, 유치원일 경우 대상자 아이디가 필요함
        if (item.getType().equals(ItemType.BEAUTY) || item.getType().equals(ItemType.HOTEL) || item.getType().equals(ItemType.KINDERGARTEN))
        {
            if (customerId == null) return false;
            if (this.customerId.equals(customerId) && this.item.equals(item))
            {
                return true;
            }
        }
        // 상품이 간식, 사료, 용품일 경우 대상자 아이디 필요하지 않음
        else
        {
            if (this.item.equals(item))
            {
                return true;
            }
        }
        return false;
    }
}

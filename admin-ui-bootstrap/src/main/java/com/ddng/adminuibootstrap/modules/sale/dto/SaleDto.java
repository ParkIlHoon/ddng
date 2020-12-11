package com.ddng.adminuibootstrap.modules.sale.dto;

import com.ddng.adminuibootstrap.modules.sale.vo.Cart;
import com.ddng.adminuibootstrap.modules.sale.vo.CartItem;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class SaleDto
{
    private Long familyId;
    private SaleType type;
    private PaymentType payment;

    private List<SaleItemDto> saleItems = new ArrayList<>();

    public SaleDto (Cart cart, SaleType saleType, PaymentType paymentType)
    {
        this.type = saleType;
        this.payment = paymentType;

        List<CartItem> cartItems = cart.getItems();
        cartItems.forEach(ci -> {
            SaleItemDto saleItemDto = SaleItemDto.builder()
                                                .itemId(ci.getItemId())
                                                .count(ci.getCount())
                                                .couponId(ci.getCouponId())
                                                .customerId(ci.getCustomerId())
                                                .scheduleId(ci.getScheduleId())
                                                .build();
            this.saleItems.add(saleItemDto);
        });
    }
}

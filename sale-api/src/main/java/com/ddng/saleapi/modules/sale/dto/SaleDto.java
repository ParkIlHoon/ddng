package com.ddng.saleapi.modules.sale.dto;

import com.ddng.saleapi.modules.sale.domain.PaymentType;
import com.ddng.saleapi.modules.sale.domain.SaleType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class SaleDto
{
    @Getter @Setter @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class Post
    {
        private Long familyId;
        private SaleType type;
        private PaymentType payment;

        private List<SaleItemDto> saleItems = new ArrayList<>();
    }
}

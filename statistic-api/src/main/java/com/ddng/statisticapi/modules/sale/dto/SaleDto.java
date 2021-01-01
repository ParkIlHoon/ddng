package com.ddng.statisticapi.modules.sale.dto;

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
    private List<SaleItemDto> saleItemList = new ArrayList<>();
}

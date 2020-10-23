package com.ddng.saleapi.modules.sale.dto;

import com.ddng.saleapi.modules.sale.domain.PaymentType;
import com.ddng.saleapi.modules.sale.domain.Sale;
import com.ddng.saleapi.modules.sale.domain.SaleItem;
import com.ddng.saleapi.modules.sale.domain.SaleType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public static class Get
    {
        private Long id;
        private Long familyId;
        private SaleType type;
        private String typeName;
        private LocalDateTime saleDate;
        private PaymentType paymentType;
        private String paymentTypeName;
        private List<SaleItemDto.Get> saleItemList = new ArrayList<>();

        public Get(Sale sale)
        {
            this.id = sale.getId();
            this.familyId = sale.getFamilyId();
            this.type = sale.getType();
            this.typeName = sale.getType().getName();
            this.saleDate = sale.getSaleDate();
            this.paymentType = sale.getPayment();
            this.paymentTypeName = sale.getPayment().getName();
            this.saleItemList = sale.getSaleItemList().stream()
                                                        .map(saleItem -> new SaleItemDto.Get(saleItem))
                                                        .collect(Collectors.toList());
        }
    }
}

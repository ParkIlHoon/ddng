package com.ddng.saleapi.modules.sale.domain;

import com.ddng.saleapi.modules.sale.dto.SaleDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SALE")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Sale
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "FAMILY_ID")
    private Long familyId;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private SaleType type;

    @Column(name = "SALE_DATE")
    private LocalDateTime saleDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT")
    private PaymentType payment;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<SaleItem> saleItemList = new ArrayList<>();

    public Sale(SaleDto.Post dto)
    {
        this.saleDate = LocalDateTime.now();
        this.payment = dto.getPayment();
        this.familyId = dto.getFamilyId();
        this.type = dto.getType();
    }

    public void addSaleItem(SaleItem saleItem)
    {
        this.saleItemList.add(saleItem);
        saleItem.setSale(this);
    }
}

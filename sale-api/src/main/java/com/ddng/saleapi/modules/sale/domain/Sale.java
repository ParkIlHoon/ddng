package com.ddng.saleapi.modules.sale.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SALE")
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

    protected Sale() { }
}

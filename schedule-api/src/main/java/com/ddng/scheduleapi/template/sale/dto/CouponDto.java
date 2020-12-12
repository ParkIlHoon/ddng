package com.ddng.scheduleapi.template.sale.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class CouponDto
{
    private Long id;
    private Long customerId;
    private String name;
    private LocalDateTime createDate;
    private LocalDateTime expireDate;
    private LocalDateTime useDate;
    private String type;
    private String typeName;
    private String itemType;
    private String itemTypeName;
}

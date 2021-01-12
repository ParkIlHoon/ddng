package com.ddng.adminuibootstrap.modules.common.dto.sale;

import lombok.*;

import java.time.LocalDateTime;

/**
 * <h1>ddng-sale-api 쿠폰 DTO</h1>
 */
@Data
@NoArgsConstructor @AllArgsConstructor
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

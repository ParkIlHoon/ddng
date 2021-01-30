package com.ddng.saleapi.modules.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>ddng-customer-api 고객 태그 DTO</h1>
 */
@Data
@NoArgsConstructor @AllArgsConstructor
public class CustomerTagDto
{
    private Long id;
    private String title;
}

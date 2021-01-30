package com.ddng.saleapi.modules.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>ddng-customer-api 가족 DTO</h1>>
 */
@Data
@NoArgsConstructor @AllArgsConstructor
public class FamilyDto
{
    private Long id;
    private String name;
    private String text;
    private List<CustomerDto> customers = new ArrayList<>();
}

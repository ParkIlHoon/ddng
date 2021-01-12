package com.ddng.adminuibootstrap.modules.common.dto.customer;

import lombok.*;

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

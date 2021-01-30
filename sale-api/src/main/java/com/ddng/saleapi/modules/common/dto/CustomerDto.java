package com.ddng.saleapi.modules.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * <h1>ddng-customer-api 고객 DTO</h1>
 */
@Data
@NoArgsConstructor @AllArgsConstructor
public class CustomerDto
{
    private Long id;
    private String name;
    private String type;
    private String typeName;
    private String telNo;
    private LocalDateTime joinDate;
    private String bigo;
    private String profileImg;
    private String sexGb;
    private Set<CustomerTagDto> tags = new HashSet<>();
    private FamilyDto family;
    private String familyString;
}

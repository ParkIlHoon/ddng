package com.ddng.adminuibootstrap.modules.common.dto.customer;

import javax.validation.constraints.NotBlank;
import lombok.*;

/**
 * <h1>ddng-customer-api 고객 태그 DTO</h1>
 */
@Data
@NoArgsConstructor @AllArgsConstructor
public class CustomerTagDto
{
    private Long id;
    @NotBlank
    private String title;
}

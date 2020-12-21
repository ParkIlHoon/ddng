package com.ddng.adminuibootstrap.modules.customer.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class CustomerTagDto
{
    private Long id;
    private String title;
}

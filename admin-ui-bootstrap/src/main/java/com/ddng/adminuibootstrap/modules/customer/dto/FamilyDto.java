package com.ddng.adminuibootstrap.modules.customer.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class FamilyDto
{
    private Long id;
    private String name;
    private String text;
}

package com.ddng.adminuibootstrap.modules.customer.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter @Setter @Data
@NoArgsConstructor @AllArgsConstructor
//@EqualsAndHashCode(of = "id")
public class FamilyDto
{
    private Long id;
    private String name;
    private String text;
//    private List<CustomerDto> customers = new ArrayList<>();
}

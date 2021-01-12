package com.ddng.adminuibootstrap.modules.common.dto.customer;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <h1>ddng-customer-api 고객 종류 DTO</h1>
 */
@Data
@NoArgsConstructor
public class CustomerTypeDto
{
    private String name;
    private String korName;

    private String id;
    private String text;

    public CustomerTypeDto(String name, String korName)
    {
        this.name = name;
        this.korName = korName;
        this.id = name;
        this.text = korName;
    }

    public void setName(String name) {
        this.name = name;
        this.id = name;
    }

    public void setKorName(String korName) {
        this.korName = korName;
        this.text = korName;
    }
}

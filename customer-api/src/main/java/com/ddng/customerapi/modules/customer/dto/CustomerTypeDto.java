package com.ddng.customerapi.modules.customer.dto;


import com.ddng.customerapi.modules.customer.domain.CustomerType;
import lombok.Data;

@Data
public class CustomerTypeDto
{
    private String name;
    private String korName;

    public CustomerTypeDto(CustomerType type)
    {
        this.name = type.name();
        this.korName = type.getKorName();
    }
}

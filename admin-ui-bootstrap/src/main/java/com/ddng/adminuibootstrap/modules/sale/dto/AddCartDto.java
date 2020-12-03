package com.ddng.adminuibootstrap.modules.sale.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AddCartDto
{
    private Long scheduleId;
    private List<Long> beauties = new ArrayList<>();
    private Long couponId;
}

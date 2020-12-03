package com.ddng.adminuibootstrap.modules.sale.dto;

import com.ddng.adminuibootstrap.modules.item.dto.ItemDto;
import com.ddng.adminuibootstrap.modules.schedules.dto.ScheduleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CartDto
{
    private ScheduleDto schedule;
    private List<ItemDto> items = new ArrayList<>();
    private CouponDto coupon;
}

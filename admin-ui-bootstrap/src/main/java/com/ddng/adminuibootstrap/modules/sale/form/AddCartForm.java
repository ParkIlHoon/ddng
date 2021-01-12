package com.ddng.adminuibootstrap.modules.sale.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AddCartForm
{
    private Long scheduleId;
    private List<Long> itemIds = new ArrayList<>();
    private Long couponId;
}

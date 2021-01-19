package com.ddng.adminuibootstrap.modules.sale.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
public class NewCouponFormWrapper
{
    List<NewCouponForm> newCouponForms = new ArrayList<>();
}

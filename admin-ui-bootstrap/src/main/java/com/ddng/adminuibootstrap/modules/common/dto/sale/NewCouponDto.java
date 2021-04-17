package com.ddng.adminuibootstrap.modules.common.dto.sale;

import com.ddng.adminuibootstrap.modules.common.dto.sale.CouponType;
import com.ddng.adminuibootstrap.modules.sale.form.NewCouponForm;
import lombok.Data;

@Data
public class NewCouponDto
{
    private Long customerId;
    private Long itemId;
    private CouponType type;

    public NewCouponDto(NewCouponForm form)
    {
        this.customerId = form.getCustomerId();
        this.itemId = form.getItemId();
        this.type = form.getCouponType();
    }
}

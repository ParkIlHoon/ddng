package com.ddng.adminuibootstrap.modules.sale.form;

import com.ddng.adminuibootstrap.modules.common.dto.customer.CustomerDto;
import com.ddng.adminuibootstrap.modules.common.dto.sale.CouponType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class NewCouponForm
{
    @NotNull
    private Long customerId;
    private String customerName;
    private String customerTypeName;
    private String customerProfileImg;

    @NotNull
    private Long itemId;
    @NotBlank
    private CouponType couponType;

    public NewCouponForm(CustomerDto customerDto)
    {
        this.customerId = customerDto.getId();
        this.customerName = customerDto.getName();
        this.customerTypeName = customerDto.getTypeName();
        this.customerProfileImg = customerDto.getProfileImg();
    }
}

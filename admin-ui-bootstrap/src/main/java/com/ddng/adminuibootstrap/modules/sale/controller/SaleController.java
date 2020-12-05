package com.ddng.adminuibootstrap.modules.sale.controller;

import com.ddng.adminuibootstrap.modules.item.dto.ItemDto;
import com.ddng.adminuibootstrap.modules.item.template.ItemTemplate;
import com.ddng.adminuibootstrap.modules.sale.dto.AddCartDto;
import com.ddng.adminuibootstrap.modules.sale.dto.CartDto;
import com.ddng.adminuibootstrap.modules.sale.dto.CouponDto;
import com.ddng.adminuibootstrap.modules.sale.template.SaleTemplate;
import com.ddng.adminuibootstrap.modules.schedules.dto.ScheduleDto;
import com.ddng.adminuibootstrap.modules.schedules.template.ScheduleTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/sale")
@SessionAttributes({"cart"})
@RequiredArgsConstructor
public class SaleController
{
    private final ScheduleTemplate scheduleTemplate;
    private final ItemTemplate itemTemplate;
    private final SaleTemplate saleTemplate;

    @GetMapping
    public String main (Model model)
    {
        return "sale/main";
    }

    @PostMapping("/cart")
    public String addCart (@RequestBody @Valid AddCartDto dto,
                           Errors errors,
                           @ModelAttribute List<CartDto> cart,
                           Model model)
    {
        if (errors.hasErrors())
        {
            model.addAttribute("errorMsg", "오류 발생");
        }

        // 스케쥴 조회
        ScheduleDto schedule = scheduleTemplate.getSchedule(dto.getScheduleId());

        // 선택한 상품목록 조회
        List<ItemDto> items = new ArrayList<>();
        for (Long itemId : dto.getItemIds())
        {
            ItemDto item = itemTemplate.getItem(itemId);
            items.add(item);
        }

        // 쿠폰 목록 조회
        CouponDto coupon = saleTemplate.getCounpon(dto.getCouponId());

        // 카트 추가
        CartDto cartDto = new CartDto();
        if (schedule != null)
        {
            cartDto.setSchedule(schedule);
        }
        if (items.size() > 0)
        {
            cartDto.setItems(items);
        }
        if (coupon != null)
        {
            cartDto.setCoupon(coupon);
        }
        cart.add(cartDto);

        return "sale/main";
    }
}

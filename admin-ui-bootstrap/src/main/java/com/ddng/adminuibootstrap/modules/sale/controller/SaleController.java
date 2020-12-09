package com.ddng.adminuibootstrap.modules.sale.controller;

import com.ddng.adminuibootstrap.modules.item.dto.ItemDto;
import com.ddng.adminuibootstrap.modules.item.template.ItemTemplate;
import com.ddng.adminuibootstrap.modules.sale.dto.AddCartDto;
import com.ddng.adminuibootstrap.modules.sale.dto.CouponDto;
import com.ddng.adminuibootstrap.modules.sale.template.SaleTemplate;
import com.ddng.adminuibootstrap.modules.sale.vo.Cart;
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

    @ModelAttribute("cart")
    public Cart cart ()
    {
        return new Cart();
    }

    @GetMapping
    public String main (Model model, @ModelAttribute Cart cart)
    {
        List<ItemDto> hotelItems = itemTemplate.getHotelItems();
        List<ItemDto> kindergartenItems = itemTemplate.getKindergartenItems();

        model.addAttribute("hotelItems", hotelItems);
        model.addAttribute("kindergartenItems", kindergartenItems);
        return "sale/main";
    }

    @PostMapping("/cart")
    public String addCart (@RequestBody @Valid AddCartDto dto,
                           Errors errors,
                           @ModelAttribute Cart cart,
                           Model model)
    {
        if (errors.hasErrors())
        {
            model.addAttribute("errorMsg", "오류 발생");
        }

        // 스케쥴 조회
        ScheduleDto schedule = null;
        if (dto.getScheduleId() != null)
        {
            schedule = scheduleTemplate.getSchedule(dto.getScheduleId());
        }

        // 선택한 상품목록 조회
        List<ItemDto> items = new ArrayList<>();
        for (Long itemId : dto.getItemIds())
        {
            ItemDto item = itemTemplate.getItem(itemId);
            if (item != null)
            {
                items.add(item);
            }
        }

        // 쿠폰 목록 조회
        CouponDto coupon = null;
        if (dto.getCouponId() != null)
        {
            coupon = saleTemplate.getCounpon(dto.getCouponId());
        }

        // 카트 추가
        if (items.size() > 0)
        {
            for(ItemDto item : items)
            {
                if (coupon == null)
                {
                    if (schedule == null)
                    {
                        cart.addCartItem(item);
                    }
                    else
                    {
                        cart.addCartItem(item, schedule);
                    }
                }
                else
                {
                    cart.addCartItem(item, schedule, coupon);
                }
            }
        }
        else
        {
            throw new RuntimeException("추가할 상품이 존재하지 않습니다.");
        }

        return "sale/main :: #item-list";
    }
}

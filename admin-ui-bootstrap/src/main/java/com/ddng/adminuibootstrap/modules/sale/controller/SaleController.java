package com.ddng.adminuibootstrap.modules.sale.controller;

import com.ddng.adminuibootstrap.modules.item.dto.ItemDto;
import com.ddng.adminuibootstrap.modules.item.template.ItemTemplate;
import com.ddng.adminuibootstrap.modules.sale.dto.AddCartDto;
import com.ddng.adminuibootstrap.modules.sale.dto.CouponDto;
import com.ddng.adminuibootstrap.modules.sale.dto.PaymentType;
import com.ddng.adminuibootstrap.modules.sale.dto.SaleType;
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

    /**
     * 결제 화면 폼 요청
     * @param model
     * @param cart
     * @return
     */
    @GetMapping
    public String main (Model model, @ModelAttribute Cart cart)
    {
        List<ItemDto> hotelItems = itemTemplate.getHotelItems();
        List<ItemDto> kindergartenItems = itemTemplate.getKindergartenItems();

        model.addAttribute("hotelItems", hotelItems);
        model.addAttribute("kindergartenItems", kindergartenItems);
        return "sale/main";
    }

    /**
     * 카트에 상품 추가 요청
     * @param dto
     * @param errors
     * @param cart
     * @param model
     * @return
     */
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
            coupon = saleTemplate.getCoupon(dto.getCouponId());
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

    /**
     * 카트 초기화 요청
     * @param cart
     * @param model
     * @return
     */
    @DeleteMapping("/cart")
    public String resetCart (@ModelAttribute Cart cart,
                             Model model)
    {
        cart.reset();
        return "sale/main :: #item-list";
    }

    /**
     * 카트 총 금액 요청
     * @param cart
     * @param model
     * @return
     */
    @GetMapping("/cart/totalPrice")
    public String refreshTotalPrice (@ModelAttribute Cart cart,
                                     Model model)
    {
        return "sale/main :: #total-price";
    }

    /**
     * 카트에 담긴 상품을 결제한다.
     * @param cart
     * @param model
     * @return
     */
    @PostMapping
    public String saleCart (SaleType saleType,
                            PaymentType paymentType,
                            @ModelAttribute Cart cart,
                            Model model)
    {
        // 판매 처리
        saleTemplate.saleCart(cart, saleType, paymentType);
        // 카트 초기화
        cart.reset();
        return "sale/main :: #item-list";
    }
}

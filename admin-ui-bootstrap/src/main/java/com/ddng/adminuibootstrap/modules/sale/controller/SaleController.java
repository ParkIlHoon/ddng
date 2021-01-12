package com.ddng.adminuibootstrap.modules.sale.controller;

import com.ddng.adminuibootstrap.modules.common.dto.RestPageImpl;
import com.ddng.adminuibootstrap.modules.common.dto.customer.CustomerDto;
import com.ddng.adminuibootstrap.modules.common.dto.sale.*;
import com.ddng.adminuibootstrap.modules.customer.template.CustomerTemplate;
import com.ddng.adminuibootstrap.modules.item.template.ItemTemplate;
import com.ddng.adminuibootstrap.modules.sale.form.ScheduleToSaleForm;
import com.ddng.adminuibootstrap.modules.sale.form.AddCartForm;
import com.ddng.adminuibootstrap.modules.sale.template.SaleTemplate;
import com.ddng.adminuibootstrap.modules.sale.vo.Cart;
import com.ddng.adminuibootstrap.modules.common.dto.schedule.ScheduleDto;
import com.ddng.adminuibootstrap.modules.schedules.template.ScheduleTemplate;
import lombok.RequiredArgsConstructor;
import org.codehaus.jettison.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sale")
@SessionAttributes({"cart"})
@RequiredArgsConstructor
public class SaleController
{
    private final ScheduleTemplate scheduleTemplate;
    private final ItemTemplate itemTemplate;
    private final SaleTemplate saleTemplate;
    private final CustomerTemplate customerTemplate;

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
    public String main (Model model, @ModelAttribute Cart cart) throws JSONException
    {
        // 호텔, 유치원 상품 조회
        List<ItemDto> hotelItems = itemTemplate.getHotelItems();
        List<ItemDto> kindergartenItems = itemTemplate.getKindergartenItems();

        // 오늘 결제 대상 조회
        List<ScheduleDto> schedules = scheduleTemplate.getNotPayedSchedules();

        if(schedules.size() > 0)
        {
            // 결제 대상들의 고객 조회
            List<Long> customerIds = new ArrayList<>();
            schedules.forEach(c -> customerIds.add(c.getCustomerId()));
            List<Long> uniqueIds = new ArrayList<Long>(new HashSet<>(customerIds));
            List<CustomerDto> customers = customerTemplate.getCustomers(uniqueIds);
            List<ScheduleToSaleForm> collect = schedules.stream().map(s -> {
                Optional<CustomerDto> first = customers.stream().filter(c -> c.getId().equals(s.getCustomerId())).findFirst();
                return new ScheduleToSaleForm(s, first.get());
            }).collect(Collectors.toList());
            model.addAttribute("schedules", collect);
        }

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
    public String addCart (@RequestBody @Valid AddCartForm dto,
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

    /**
     * 상품 검색 요청
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/items")
    public ResponseEntity searchItems(String keyword, int page, int size)
    {
        if (StringUtils.hasText(keyword))
        {
            RestPageImpl<ItemDto> restPage = itemTemplate.searchItemsWithPage(keyword, page, size);
            return ResponseEntity.ok(restPage);
        }

        return ResponseEntity.badRequest().build();
    }

    /**
     * 고객 조회 요청
     * @param id 조회할 고객 아이디
     * @return
     */
    @GetMapping("/customers/{id}")
    public ResponseEntity searchCustomer(@PathVariable("id") Long id)
    {
        if (id == null)
        {
            return ResponseEntity.badRequest().build();
        }

        CustomerDto customer = customerTemplate.getCustomer(id);
        return ResponseEntity.ok(customer);
    }

    /**
     * 스케쥴 조회 요청
     * @param id 조회할 스케쥴 아이디
     * @return
     */
    @GetMapping("/schedules/{id}")
    public ResponseEntity searchSchedules(@PathVariable("id") Long id)
    {
        if (id == null)
        {
            return ResponseEntity.badRequest().build();
        }

        ScheduleDto schedule = scheduleTemplate.getSchedule(id);
        return ResponseEntity.ok(schedule);
    }

    /**
     * 미용 상품 검색 요청
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/items/beauty")
    public ResponseEntity searchBeautyItems(String keyword, int page, int size)
    {
        if (StringUtils.isEmpty(keyword))
        {
            return ResponseEntity.badRequest().build();
        }

        RestPageImpl<ItemDto> beautyItems = saleTemplate.getBeautyItems(keyword, page, size);
        return ResponseEntity.ok(beautyItems);
    }
}

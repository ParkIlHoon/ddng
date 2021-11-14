package com.ddng.adminuibootstrap.modules.sale.controller;

import com.ddng.adminuibootstrap.modules.common.clients.CustomerClient;
import com.ddng.adminuibootstrap.modules.common.clients.SaleClient;
import com.ddng.adminuibootstrap.modules.common.clients.ScheduleClient;
import com.ddng.adminuibootstrap.modules.common.dto.FeignPageImpl;
import com.ddng.adminuibootstrap.modules.common.dto.customer.CustomerDto;
import com.ddng.adminuibootstrap.modules.common.dto.sale.NewCouponDto;
import com.ddng.adminuibootstrap.modules.common.dto.sale.*;
import com.ddng.adminuibootstrap.modules.common.enums.AlertType;
import com.ddng.adminuibootstrap.modules.common.utils.AlertUtils;
import com.ddng.adminuibootstrap.modules.sale.form.NewCouponForm;
import com.ddng.adminuibootstrap.modules.sale.form.NewCouponFormWrapper;
import com.ddng.adminuibootstrap.modules.sale.form.ScheduleToSaleForm;
import com.ddng.adminuibootstrap.modules.sale.form.AddCartForm;
import com.ddng.adminuibootstrap.modules.sale.vo.Cart;
import com.ddng.adminuibootstrap.modules.common.dto.schedule.ScheduleDto;
import com.ddng.adminuibootstrap.modules.sale.vo.CartItem;
import java.util.Map;
import java.util.function.Function;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.codehaus.jettison.json.JSONException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <h1>판매 메뉴 컨트롤러</h1>
 */
@Controller
@RequestMapping("/sale")
@SessionAttributes({"cart"})
@RequiredArgsConstructor
public class SaleController {

    private final ScheduleClient scheduleClient;
    private final SaleClient saleClient;
    private final CustomerClient customerClient;

    @ModelAttribute("cart")
    public Cart cart() {
        return new Cart();
    }

    /**
     * 결제 화면 폼 요청
     */
    @GetMapping
    public String main(Model model, @ModelAttribute Cart cart) {
        // 호텔, 유치원 상품 조회
        List<ItemDto> hotelItems = saleClient.getHotelItems();
        List<ItemDto> kindergartenItems = saleClient.getKindergartenItems();

        // 오늘 결제 대상 조회
        List<ScheduleDto> schedules = scheduleClient.getCertainDaySchedule(LocalDate.now().toString(), false);

        if (!schedules.isEmpty()) {
            // 결제 대상들의 고객 조회
            List<Long> customerIds = schedules.stream().map(ScheduleDto::getCustomerId).distinct().collect(Collectors.toList());
            Map<Long, CustomerDto> customers = customerClient.getCustomers(customerIds).stream().collect(Collectors.toMap(CustomerDto::getId, Function.identity()));
            List<ScheduleToSaleForm> collect = schedules.stream().map(s -> new ScheduleToSaleForm(s, customers.get(s.getCustomerId()))).collect(Collectors.toList());
            model.addAttribute("schedules", collect);
        }

        // 이전 결제 목록 조회
        List<SaleDto> saleHistories = saleClient.searchSale(LocalDate.now().atStartOfDay().toString(), LocalDateTime.now().toString(), SaleType.PAYED, 0, 50).getContent();

        model.addAttribute("hotelItems", hotelItems);
        model.addAttribute("kindergartenItems", kindergartenItems);
        model.addAttribute("saleHistories", saleHistories);
        return "sale/main";
    }

    /**
     * 카트에 상품 추가 요청
     */
    @PostMapping("/cart")
    public String addCart(@Valid
                            @RequestBody  AddCartForm dto,
                            Errors errors,
                            @ModelAttribute Cart cart,
                            Model model) {
        if (errors.hasErrors()) {
            AlertUtils.alertFail(model, "오류가 발생해 카트에 상품을 추가하지 못했습니다.");
        }

        // 스케쥴 조회
        ScheduleDto schedule = null;
        if (dto.getScheduleId() != null) {
            schedule = scheduleClient.getSchedule(dto.getScheduleId());
        }

        // 선택한 상품목록 조회
        List<ItemDto> items = new ArrayList<>();
        for (Long itemId : dto.getItemIds()) {
            ItemDto item = saleClient.getItem(itemId);
            if (item != null) {
                items.add(item);
            }
        }

        // 쿠폰 목록 조회
        CouponDto coupon = null;
        if (dto.getCouponId() != null) {
            coupon = saleClient.getCoupon(dto.getCouponId());
        }

        // 카트 추가
        if (!items.isEmpty()) {
            for (ItemDto item : items) {
                if (coupon == null) {
                    if (schedule == null) {
                        cart.addCartItem(item);
                    } else {
                        cart.addCartItem(item, schedule);
                    }
                } else {
                    cart.addCartItem(item, schedule, coupon);
                }
            }
        } else {
            AlertUtils.alertFail(model, "추가할 상품이 존재하지 않습니다.");
        }

        return "sale/main :: #item-list";
    }

    /**
     * 카트 초기화 요청
     */
    @DeleteMapping("/cart")
    public String resetCart(@ModelAttribute Cart cart, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "sale/main :: #item-list";
    }

    /**
     * 카트의 특정 상품 제거 요청
     *
     * @param cart   카트
     * @param itemId 제거할 상품의 아이디
     */
    @DeleteMapping("/cart/{itemId}")
    public String removeItemFromCart(@ModelAttribute Cart cart,
                                     @PathVariable("itemId") Long itemId) {
        if (itemId != null) {
            cart.removeCartItem(itemId);
        }
        return "sale/main :: #item-list";
    }

    /**
     * 카트 총 금액 요청
     */
    @GetMapping("/cart/totalPrice")
    public String refreshTotalPrice(@ModelAttribute Cart cart) {
        return "sale/main :: #total-price";
    }

    /**
     * 카트에 담긴 상품을 결제한다.
     */
    @PostMapping
    public String saleCart(SaleType saleType,
                            PaymentType paymentType,
                            @ModelAttribute Cart cart,
                            RedirectAttributes attributes,
                            SessionStatus sessionStatus) {
        // 판매 처리
        ResponseEntity responseEntity = saleClient.saleCart(new NewSaleDto(cart, saleType, paymentType));
        HttpStatus status = responseEntity.getStatusCode();

        if (status.is2xxSuccessful()) {
            String cartItemNames = cart.getItems().stream().map(CartItem::getItemName).collect(Collectors.joining(", "));
            int totalPrice = cart.getTotalPrice();
            // 카트 초기화
            cart.reset();
            // 쿠폰 적립 가능한 사용자 목록 조회
            List<Long> customerIds = saleClient.getCouponIssuableCustomers();
            if (!customerIds.isEmpty()) {
                List<CustomerDto> customers = customerClient.getCustomers(customerIds);
                List<NewCouponForm> collect = customers.stream().map(NewCouponForm::new).collect(Collectors.toList());
                attributes.addFlashAttribute("couponIssuableCustomers", new NewCouponFormWrapper(collect));
                attributes.addFlashAttribute("couponTypes", CouponType.values());
            }
            AlertUtils.alertSuccess(attributes, "결제가 정상적으로 완료되었습니다. (총 금액 : " + totalPrice + "원 / 결제수단 : " + paymentType.getName() + " / " + cartItemNames + ")");
        } else {
            AlertUtils.alertFail(attributes, "결제 중 오류가 발생했습니다.");
        }
        sessionStatus.setComplete();
        return "redirect:/sale";
    }

    /**
     * 상품 검색 요청
     */
    @GetMapping("/items")
    public ResponseEntity searchItems(String keyword, @Min(0) int page, @Min(1) int size) {
        if (StringUtils.hasText(keyword)) {
            FeignPageImpl<ItemDto> itemsWithPage = saleClient.searchItemsWithPage(keyword, page, size);
            return ResponseEntity.ok(itemsWithPage);
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * 고객 조회 요청
     *
     * @param id 조회할 고객 아이디
     */
    @GetMapping("/customers/{id}")
    public ResponseEntity searchCustomer(@PathVariable("id") Long id) {
        CustomerDto customer = customerClient.getCustomer(id);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customer);
    }

    /**
     * 스케쥴 조회 요청
     *
     * @param id 조회할 스케쥴 아이디
     */
    @GetMapping("/schedules/{id}")
    public ResponseEntity searchSchedules(@PathVariable("id") Long id) {
        ScheduleDto schedule = scheduleClient.getSchedule(id);
        if (schedule == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(schedule);
    }

    /**
     * 미용 상품 검색 요청
     */
    @GetMapping("/items/beauty")
    public ResponseEntity searchBeautyItems(String keyword, @Min(0) int page, @Min(1) int size) {
        if (StringUtils.isEmpty(keyword)) {
            return ResponseEntity.badRequest().build();
        }

        FeignPageImpl<ItemDto> beautyItemsWithPage = saleClient.getBeautyItemsWithPage(keyword, page, size);
        return ResponseEntity.ok(beautyItemsWithPage);
    }

    /**
     * 쿠폰 발급 요청
     */
    @PostMapping("/coupons")
    public String issueNewCoupons(@ModelAttribute
                                    @Valid NewCouponFormWrapper couponFormWrapper,
                                    Errors errors,
                                    RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            AlertUtils.alertFail(attributes, errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(",")));
            return "redirect:/sale";
        }

        List<NewCouponForm> newCouponForms = couponFormWrapper.getNewCouponForms();
        if (newCouponForms.isEmpty()) {
            AlertUtils.alertFail(attributes, "생성할 쿠폰 정보가 없습니다.");
            return "redirect:/sale";
        }

        HttpStatus status = HttpStatus.OK;
        for (NewCouponForm form : newCouponForms) {
            status = saleClient.issueNewCoupons(new NewCouponDto(form)).getStatusCode();
        }

        if (status.is2xxSuccessful()) {
            AlertUtils.alertSuccess(attributes, "쿠폰이 정상적으로 발급되었습니다.");
            return "redirect:/sale";
        }

        return "redirect:/sale";
    }

    /**
     * 판매 기록 조회 요청
     */
    @GetMapping("/history/{id}")
    public ResponseEntity getSaleHistory(@PathVariable("id") Long id) {
        SaleDto dto = saleClient.getSale(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    /**
     * 환불 요청
     *
     * @param id 환불할 판매 아이디
     */
    @PostMapping("/refund/{id}")
    public String refundSale(@PathVariable("id") Long id,
                                RedirectAttributes attributes) {
        SaleDto dto = saleClient.getSale(id);
        if (dto == null) {
            AlertUtils.alertFail(attributes, "존재하지 않는 판매 이력입니다.");
            return "redirect:/sale";
        }

        ResponseEntity<String> responseEntity = saleClient.refundSale(id);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            AlertUtils.alertSuccess(attributes, "정상적으로 환불되었습니다.");
            return "redirect:/sale";
        }

        return "redirect:/sale";
    }
}

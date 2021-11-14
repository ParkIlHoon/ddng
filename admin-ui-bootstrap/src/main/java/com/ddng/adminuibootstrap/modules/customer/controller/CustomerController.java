package com.ddng.adminuibootstrap.modules.customer.controller;

import com.ddng.adminuibootstrap.modules.common.clients.SaleClient;
import com.ddng.adminuibootstrap.modules.common.dto.FeignPageImpl;
import com.ddng.adminuibootstrap.modules.common.dto.customer.*;
import com.ddng.adminuibootstrap.modules.common.dto.sale.SaleItemDto;
import com.ddng.adminuibootstrap.modules.common.utils.AlertUtils;
import com.ddng.adminuibootstrap.modules.customer.form.EditForm;
import com.ddng.adminuibootstrap.modules.common.clients.CustomerClient;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>고객 관리 > 고객 조회 메뉴 컨트롤러</h1>
 */
@Controller
@RequestMapping("/customer-management/search-customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerClient customerClient;
    private final SaleClient saleClient;

    /**
     * 고객 조회 메뉴 폼 요청
     */
    @GetMapping("/search-form")
    public String searchForm() {
        return "customer/search/main";
    }

    /**
     * 고객 목록 조회 요청
     *
     * @param keyword 조회 키워드
     */
    @GetMapping("/customer-list")
    public String searchAction(String keyword,
                                @Min(0) int page,
                                @Min(1) int size,
                                Model model) {
        List<CustomerDto> searchCustomers = new ArrayList<>();
        long totalElements = 0;

        if (StringUtils.hasText(keyword)) {
            FeignPageImpl<CustomerDto> customersWithPage = customerClient.searchCustomersWithPage(keyword, page, size);
            totalElements = customersWithPage.getTotalElements();
            searchCustomers = customersWithPage.getContent();
        }

        model.addAttribute("customers", searchCustomers);
        model.addAttribute("totalElements", totalElements);
        return "customer/search/main :: #customer-list";
    }

    /**
     * 고객 조회 요청
     *
     * @param id    고객 아이디
     */
    @GetMapping("/customers/{id}")
    public String searchCustomerAction(@PathVariable("id") Long id,
                                        Model model,
                                        RedirectAttributes redirectAttributes) {
        CustomerDto customer = customerClient.getCustomer(id);
        if (customer == null) {
            AlertUtils.alertFail(redirectAttributes, "존재하지 않는 고객입니다.");
            return "redirect:/customer-management/search-customer/search-form";
        }

        List<CustomerTypeDto> customerTypes = customerClient.getCustomerTypes();
        List<CustomerTagDto> customerTags = customerClient.getCustomerTags();

        // 결제 이력 조회
        List<SaleItemDto> customerSaleItems = saleClient.getSaleHistoryByCustomerId(id);

        model.addAttribute("customerForm", new EditForm(customer));
        model.addAttribute("customerTypes", customerTypes);
        model.addAttribute("customerTags", customerTags);
        model.addAttribute("customerSaleItems", customerSaleItems);
        return "customer/search/main :: #customer-card";
    }

    /**
     * 고객 태그 추가 요청
     *
     * @param id  고객 아이디
     * @param dto 추가 태그 dto
     */
    @PostMapping("/customers/{id}/tags/add")
    public ResponseEntity customerTagAddAction(@PathVariable("id") Long id,
                                                @Valid
                                                @RequestBody CustomerTagDto dto,
                                                Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        return customerClient.addCustomerTag(id, dto.getTitle());
    }

    /**
     * 고객 태그 제거 요청
     *
     * @param id  고객 아이디
     * @param dto 제거 태그 dto
     */
    @PostMapping("/customers/{id}/tags/remove")
    public ResponseEntity customerTagRemoveAction(@PathVariable("id") Long id,
                                                    @Valid
                                                    @RequestBody CustomerTagDto dto,
                                                    Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        return customerClient.removeCustomerTag(id, dto.getTitle());
    }

    /**
     * 고객 정보 수정 요청
     *
     * @param id                 고객 아이디
     * @param editForm           수정 내용
     */
    @PostMapping("/customers/{id}")
    public String editCustomerAction(@PathVariable("id") Long id,
                                        @Valid
                                        @ModelAttribute EditForm editForm,
                                        Errors errors,
                                        RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return "customer/search/main::#customer-card";
        }
        CustomerDto updatedCustomer = customerClient.updateCustomer(id, editForm);
        AlertUtils.alertSuccess(redirectAttributes, updatedCustomer.getName() + " 고객 정보가 정상적으로 변경되었습니다.");
        return "redirect:/customer-management/search-customer/search-form";
    }
}
package com.ddng.adminuibootstrap.modules.customer.controller;

import com.ddng.adminuibootstrap.modules.common.clients.CustomerClient;
import com.ddng.adminuibootstrap.modules.common.clients.SaleClient;
import com.ddng.adminuibootstrap.modules.common.dto.FeignPageImpl;
import com.ddng.adminuibootstrap.modules.common.dto.customer.CustomerDto;
import com.ddng.adminuibootstrap.modules.common.dto.customer.FamilyDto;
import com.ddng.adminuibootstrap.modules.common.dto.sale.CouponDto;
import com.ddng.adminuibootstrap.modules.common.dto.sale.SaleDto;
import com.ddng.adminuibootstrap.modules.common.enums.AlertType;
import com.ddng.adminuibootstrap.modules.common.utils.AlertUtils;
import com.ddng.adminuibootstrap.modules.customer.form.FamilySettingForm;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>고객 관리 > 가족 관리 메뉴 컨트롤러</h1>
 */
@Controller
@RequestMapping("/customer-management/family-management")
@RequiredArgsConstructor
public class FamilyController {

    private final CustomerClient customerClient;
    private final SaleClient saleClient;

    /**
     * 가족 관리 메뉴 폼 요청
     */
    @GetMapping("/family-form")
    public String familyForm() {
        return "customer/family/main";
    }

    /**
     * 가족 카드 조회 요청
     *
     * @param keyword 조회 키워드
     * @param page    조회 페이지
     * @param size    조회 건수
     */
    @GetMapping("/family-card")
    public String familyCardAction(String keyword,
                                    @Min(0) int page,
                                    @Min(1) int size,
                                    Model model) {
        List<FamilyDto> searchFamilies = new ArrayList<>();
        long totalElements = 0;

        if (StringUtils.hasText(keyword)) {
            FeignPageImpl<FamilyDto> familiesWithPage = customerClient.searchFamiliesWithPage(keyword, page, size);
            totalElements = familiesWithPage.getTotalElements();
            searchFamilies = familiesWithPage.getContent();
        }

        model.addAttribute("families", searchFamilies);
        model.addAttribute("totalElements", totalElements);
        return "customer/family/main :: #search-result-row";
    }

    /**
     * 가족 섹션 조회 요청
     *
     * @param id 조회할 가족 아이디
     * @return
     */
    @GetMapping("/families/{id}")
    public String familySectionAction(@PathVariable("id") Long id, Model model) {
        FamilyDto family = customerClient.getFamily(id);

        if (family != null) {
            List<Long> customerIds = family.getCustomers().stream().map(CustomerDto::getId).collect(Collectors.toList());

            // 가족 구성원들의 쿠폰 목록 조회
            List<CouponDto> coupons = saleClient.getCoupons(customerIds).getContent();
            model.addAttribute("familyCoupons", coupons);

            // 결제 이력 조회
            List<SaleDto> saleHistory = saleClient.getSaleHistoryByFamilyId(family.getId());
            model.addAttribute("familySaleHistory", saleHistory);
        }

        model.addAttribute("familySection", family);
        model.addAttribute("familySettingForm", new FamilySettingForm(family));
        return "customer/family/main :: #family-section";
    }

    /**
     * 가족 설정 수정 요청
     *
     * @param id 수정할 가족 아이디
     * @return
     */
    @PostMapping("/families/{id}")
    public String familySectionAction(@PathVariable("id") Long id,
                                        @Valid
                                        @ModelAttribute FamilySettingForm familySettingForm,
                                        Errors errors,
                                        Model model) {
        if (errors.hasErrors()) {
            return "customer/family/main";
        }

        customerClient.updateFamilySetting(id, familySettingForm);

        FamilyDto family = customerClient.getFamily(id);

        if (family != null) {
            List<CustomerDto> customers = family.getCustomers();
            List<Long> collect = customers.stream().map(CustomerDto::getId).collect(Collectors.toList());
            List<CouponDto> coupons = saleClient.getCoupons(collect).getContent();
            model.addAttribute("familyCoupons", coupons);
        }

        model.addAttribute("familySection", family);
        model.addAttribute("familySettingForm", new FamilySettingForm(family));
        AlertUtils.alertSuccess(model, familySettingForm.getName() + " 가족의 설정 내용이 정상적으로 변경되었습니다.");
        return "customer/family/main";
    }
}

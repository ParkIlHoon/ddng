package com.ddng.adminuibootstrap.modules.customer.controller;

import com.ddng.adminuibootstrap.infra.RestPageImpl;
import com.ddng.adminuibootstrap.infra.properties.ServiceProperties;
import com.ddng.adminuibootstrap.modules.customer.dto.*;
import com.ddng.adminuibootstrap.modules.customer.form.EditForm;
import com.ddng.adminuibootstrap.modules.customer.form.FamilySettingForm;
import com.ddng.adminuibootstrap.modules.customer.form.RegisterForm;
import com.ddng.adminuibootstrap.modules.customer.template.CustomerTemplate;
import com.ddng.adminuibootstrap.modules.sale.dto.CouponDto;
import com.ddng.adminuibootstrap.modules.sale.template.SaleTemplate;
import lombok.RequiredArgsConstructor;
import org.codehaus.jettison.json.JSONException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>고객 관리 메뉴 컨트롤러</h1>
 */
@Controller
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController
{
    private final ServiceProperties serviceProperties;
    private final CustomerTemplate customerTemplate;
    private final SaleTemplate saleTemplate;

    /**
     * 고객 조회 메뉴 폼 요청
     * @param model
     * @return
     */
    @GetMapping("/search")
    public String searchForm (Model model)
    {
        return "customer/search/main";
    }

    /**
     * 고객 목록 조회 요청
     * @param keyword 조회 키워드
     * @return
     */
    @GetMapping("/customers")
    public String searchAction (String keyword, int page, int size, Model model)
    {
        List<CustomerDto> searchCustomers = new ArrayList<>();
        long totalElements = 0;

        if (StringUtils.hasText(keyword))
        {
            RestPageImpl<CustomerDto> customersWithPage = customerTemplate.searchCustomersWithPage(keyword, page, size);
            totalElements = customersWithPage.getTotalElements();
            searchCustomers = customersWithPage.getContent();
        }

        model.addAttribute("customers", searchCustomers);
        model.addAttribute("totalElements", totalElements);
        return "customer/search/main :: #customer-list";
    }

    /**
     * 고객 조회 요청
     * @param id 고객 아이디
     * @param model
     * @return
     */
    @GetMapping("/search/{id}")
    public String searchCustomerAction (@PathVariable("id") Long id, Model model)
    {
        if (id == null)
        {
            return "customer/search/main :: #customer-card";
        }

        CustomerDto customer = customerTemplate.getCustomer(id);
        List<CustomerTypeDto> customerTypes = customerTemplate.getCustomerTypes();
        List<CustomerTagDto> customerTags = customerTemplate.getCustomerTags();

        // 결제 이력 조회
        List<SaleItemDto> customerSaleItems = saleTemplate.getSaleHistoryByCustomerId(id);

        model.addAttribute("customerForm", new EditForm(customer));
        model.addAttribute("customerTypes", customerTypes);
        model.addAttribute("customerTags", customerTags);
        model.addAttribute("customerSaleItems", customerSaleItems);
        return "customer/search/main :: #customer-card";
    }

    /**
     * 고객 태그 추가 요청
     * @param id 고객 아이디
     * @param dto 추가 태그 dto
     * @param model
     * @return
     * @throws JSONException
     */
    @PostMapping("/{id}/tags/add")
    public ResponseEntity customerTagAddAction (@PathVariable("id") Long id,
                                                @RequestBody CustomerTagDto dto,
                                                Model model) throws JSONException
    {
        customerTemplate.addCustomerTag(id, dto);
        return ResponseEntity.ok().build();
    }

    /**
     * 고객 태그 제거 요청
     * @param id 고객 아이디
     * @param dto 제거 태그 dto
     * @param model
     * @return
     * @throws JSONException
     */
    @PostMapping("/{id}/tags/remove")
    public ResponseEntity customerTagRemoveAction (@PathVariable("id") Long id,
                                                @RequestBody CustomerTagDto dto,
                                                Model model) throws JSONException
    {
        customerTemplate.removeCustomerTag(id, dto);
        return ResponseEntity.ok().build();
    }

    /**
     * 고객 정보 수정 요청
     * @param id 고객 아이디
     * @param editForm 수정 내용
     * @param errors
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/edit/{id}")
    public String editCustomerAction (@PathVariable("id") Long id,
                                      @Valid @ModelAttribute EditForm editForm,
                                      Errors errors,
                                      RedirectAttributes redirectAttributes)
    {
        if (errors.hasErrors())
        {
            return "customer/search/main::#customer-card";
        }
        customerTemplate.updateCustomer(id, editForm);
        redirectAttributes.addFlashAttribute("alertType", "success");
        redirectAttributes.addFlashAttribute("message", editForm.getName() + " 고객 정보가 정상적으로 변경되었습니다.");
        return "redirect:/customer/search";
    }

    /**
     * 고객 등록 메뉴 폼 요청
     * @param model
     * @return
     */
    @GetMapping("/register")
    public String registerForm (Model model)
    {
        // 고객 종류 조회
        List<CustomerTypeDto> customerTypes = customerTemplate.getCustomerTypes();

        model.addAttribute("customerTypes", customerTypes);
        model.addAttribute("registerForm", new RegisterForm());
        return "customer/register/main";
    }

    /**
     * 고객 등록 액션 요청
     * @param registerForm
     * @param errors
     * @return
     * @throws Exception
     */
    @PostMapping("/register")
    public String registerAction (@Valid @ModelAttribute RegisterForm registerForm,
                                  Errors errors,
                                  Model model,
                                  RedirectAttributes redirectAttributes) throws Exception
    {
        if (errors.hasErrors())
        {
            String message = "";
            List<ObjectError> allErrors = errors.getAllErrors();
            for (ObjectError error : allErrors)
            {
                message += error.getDefaultMessage() + "\n";
            }

            model.addAttribute("alertType", "danger");
            model.addAttribute("message", message);
            return "customer/register/main";
        }
        customerTemplate.createCustomer(registerForm);
        redirectAttributes.addFlashAttribute("alertType", "success");
        redirectAttributes.addFlashAttribute("message", registerForm.getName() + " 고객이 정상적으로 생성되었습니다.");
        return "redirect:/customer/register";
    }

    /**
     * 가족 목록 조회
     * @param keyword
     * @return
     */
    @GetMapping("/families")
    public ResponseEntity familiesAction (String keyword)
    {
        if (StringUtils.hasText(keyword))
        {
            List<FamilyDto> searchFamilies = customerTemplate.searchFamilies(keyword);
            return ResponseEntity.ok(searchFamilies);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * 가족 관리 메뉴 폼 요청
     * @param model
     * @return
     */
    @GetMapping("/family")
    public String familyForm (Model model)
    {
        return "customer/family/main";
    }

    /**
     * 가족 카드 조회 요청
     * @param keyword 조회 키워드
     * @param page 조회 페이지
     * @param size 조회 건수
     * @param model
     * @return
     */
    @GetMapping("/familyCard")
    public String familyCardAction (String keyword, int page, int size, Model model)
    {
        List<FamilyDto> searchFamilies = new ArrayList<>();
        long totalElements = 0;

        if (StringUtils.hasText(keyword))
        {
            RestPageImpl<FamilyDto> familyRestPage = customerTemplate.searchFamiliesWithPage(keyword, page, size);
            totalElements = familyRestPage.getTotalElements();
            searchFamilies = familyRestPage.getContent();
        }

        model.addAttribute("families", searchFamilies);
        model.addAttribute("totalElements", totalElements);
        return "customer/family/main :: #search-result-row";
    }

    /**
     * 가족 섹션 조회 요청
     * @param id 조회할 가족 아이디
     * @return
     */
    @GetMapping("/families/{id}")
    public String familySectionAction (@PathVariable("id") Long id, Model model)
    {
        if (id == null)
        {
            return "customer/family/main :: #family-section";
        }

        FamilyDto family = customerTemplate.getFamily(id);

        if (family != null)
        {
            List<Long> customerIds = family.getCustomers().stream().map(c -> c.getId()).collect(Collectors.toList());

            // 가족 구성원들의 쿠폰 목록 조회
            List<CouponDto> coupons = saleTemplate.getCoupons(customerIds).getContent();
            model.addAttribute("familyCoupons", coupons);

            // 결제 이력 조회
            List<SaleDto> saleHistory = saleTemplate.getSaleHistoryByFamilyId(family.getId());
            model.addAttribute("familySaleHistory", saleHistory);
        }

        model.addAttribute("familySection", family);
        model.addAttribute("familySettingForm", new FamilySettingForm(family));
        return "customer/family/main :: #family-section";
    }

    /**
     * 가족 설정 수정 요청
     * @param id 수정할 가족 아이디
     * @return
     */
    @PostMapping("/families/{id}")
    public String familySectionAction (@PathVariable("id") Long id,
                                       @Valid @ModelAttribute FamilySettingForm familySettingForm,
                                       Errors errors,
                                       Model model)
    {
        if(errors.hasErrors())
        {
            return "customer/family/main";
        }

        customerTemplate.updateFamilySetting(familySettingForm);

        FamilyDto family = customerTemplate.getFamily(id);

        if (family != null)
        {
            List<CustomerDto> customers = family.getCustomers();
            List<Long> collect = customers.stream().map(c -> c.getId()).collect(Collectors.toList());
            List<CouponDto> coupons = saleTemplate.getCoupons(collect).getContent();
            model.addAttribute("familyCoupons", coupons);
        }

        model.addAttribute("familySection", family);
        model.addAttribute("familySettingForm", new FamilySettingForm(family));
        model.addAttribute("alertType", "success");
        model.addAttribute("message", familySettingForm.getName() + " 가족의 설정 내용이 정상적으로 변경되었습니다.");
        return "customer/family/main";
    }



    /**
     * 고객 사진 요청
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/**")
    public ResponseEntity getFile (HttpServletRequest request) throws Exception
    {
        String filePath = serviceProperties.getFile() + File.separator + StringUtils.cleanPath(request.getRequestURI().replace("/customer/", ""));

        File file = new File(filePath);

        if (file.exists())
        {
            String contentType = "";
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            request.getServletContext().getMimeType(file.getAbsolutePath());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }
}
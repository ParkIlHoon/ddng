package com.ddng.adminuibootstrap.modules.customer.controller;

import com.ddng.adminuibootstrap.modules.common.dto.RestPageImpl;
import com.ddng.adminuibootstrap.infra.properties.ServiceProperties;
import com.ddng.adminuibootstrap.modules.common.dto.customer.*;
import com.ddng.adminuibootstrap.modules.customer.form.EditForm;
import com.ddng.adminuibootstrap.modules.customer.template.CustomerTemplate;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>고객 관리 > 고객 조회 메뉴 컨트롤러</h1>
 */
@Controller
@RequestMapping("/customer-management/search-customer")
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
    @GetMapping("/search-form")
    public String searchForm (Model model)
    {
        return "customer/search/main";
    }

    /**
     * 고객 목록 조회 요청
     * @param keyword 조회 키워드
     * @return
     */
    @GetMapping("/customer-list")
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
    @GetMapping("/customers/{id}")
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
    @PostMapping("/customers/{id}/tags/add")
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
    @PostMapping("/customers/{id}/tags/remove")
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
    @PostMapping("/customers/{id}")
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
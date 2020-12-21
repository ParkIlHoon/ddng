package com.ddng.adminuibootstrap.modules.customer.controller;

import com.ddng.adminuibootstrap.infra.RestPageImpl;
import com.ddng.adminuibootstrap.infra.properties.ServiceProperties;
import com.ddng.adminuibootstrap.modules.customer.dto.CustomerTypeDto;
import com.ddng.adminuibootstrap.modules.customer.dto.FamilyDto;
import com.ddng.adminuibootstrap.modules.customer.form.FamilySettingForm;
import com.ddng.adminuibootstrap.modules.customer.form.RegisterForm;
import com.ddng.adminuibootstrap.modules.customer.template.CustomerTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

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
                                  RedirectAttributes redirectAttributes) throws Exception
    {
        if (errors.hasErrors())
        {
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
    public ResponseEntity familyAction (String keyword)
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
        model.addAttribute("familySettingForm", new FamilySettingForm());
        model.addAttribute("families", new ArrayList<>());
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
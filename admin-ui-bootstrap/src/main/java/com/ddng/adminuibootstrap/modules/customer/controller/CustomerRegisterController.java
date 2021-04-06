package com.ddng.adminuibootstrap.modules.customer.controller;

import com.ddng.adminuibootstrap.modules.common.dto.customer.CustomerTypeDto;
import com.ddng.adminuibootstrap.modules.common.dto.customer.FamilyDto;
import com.ddng.adminuibootstrap.modules.customer.form.RegisterForm;
import com.ddng.adminuibootstrap.modules.customer.template.CustomerClient;
import com.ddng.adminuibootstrap.modules.customer.template.CustomerTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

/**
 * <h1>고객 관리 > 고객 등록 메뉴 컨트롤러</h1>
 */
@Controller
@RequestMapping("/customer-management/customer-register")
@RequiredArgsConstructor
public class CustomerRegisterController
{
    private final CustomerTemplate customerTemplate;
    private final CustomerClient customerClient;

    /**
     * 고객 등록 메뉴 폼 요청
     * @param model
     * @return
     */
    @GetMapping("/register-form")
    public String registerForm (Model model)
    {
        // 고객 종류 조회
        List<CustomerTypeDto> customerTypes = customerClient.getCustomerTypes();
//        List<CustomerTypeDto> customerTypes = customerTemplate.getCustomerTypes();

        model.addAttribute("customerTypes", customerTypes);
        model.addAttribute("registerForm", new RegisterForm());
        return "customer/register/main";
    }

    /**
     * 가족 목록 조회
     * @param keyword
     * @return
     */
    @GetMapping("/family-list")
    public ResponseEntity familiesAction (String keyword)
    {
        if (StringUtils.hasText(keyword))
        {
            List<FamilyDto> searchFamilies = customerClient.searchFamilies(keyword).getContent();
//            List<FamilyDto> searchFamilies = customerTemplate.searchFamilies(keyword);
            return ResponseEntity.ok(searchFamilies);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * 고객 등록 액션 요청
     * @param registerForm
     * @param errors
     * @return
     * @throws Exception
     */
    @PostMapping("/customers")
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
        return "redirect:/customer-management/customer-register/register-form";
    }
}

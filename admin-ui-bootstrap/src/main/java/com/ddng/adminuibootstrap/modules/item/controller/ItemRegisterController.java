package com.ddng.adminuibootstrap.modules.item.controller;

import com.ddng.adminuibootstrap.modules.common.dto.sale.ItemTypeDto;
import com.ddng.adminuibootstrap.modules.item.form.RegisterForm;
import com.ddng.adminuibootstrap.modules.item.template.ItemTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
 * <h1>상품 관리 > 상품 등록 메뉴 컨트롤러</h1>
 */
@Controller
@RequestMapping("/item-management/item-register")
@RequiredArgsConstructor
public class ItemRegisterController
{
    private final ItemTemplate itemTemplate;

    /**
     * 상품 등록 메뉴 폼 요청
     * @param model
     * @return
     */
    @GetMapping("/register-form")
    public String registerForm (Model model)
    {
        List<ItemTypeDto> types = itemTemplate.getTypes();
        model.addAttribute("types", types);
        model.addAttribute("registerForm", new RegisterForm());
        return "item/register/main";
    }

    /**
     * 상픔 등록 요청
     * @param registerForm 등록할 내용
     * @param errors
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/items")
    public String registerAction (@Valid @ModelAttribute RegisterForm registerForm,
                                  Errors errors,
                                  Model model,
                                  RedirectAttributes redirectAttributes)
    {
        if(errors.hasErrors())
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

        itemTemplate.createItem(registerForm);
        redirectAttributes.addFlashAttribute("alertType", "success");
        redirectAttributes.addFlashAttribute("message", registerForm.getName() + " 상품이 정상적으로 생성되었습니다.");
        return "redirect:/item-management/item-register/register-form";
    }
}

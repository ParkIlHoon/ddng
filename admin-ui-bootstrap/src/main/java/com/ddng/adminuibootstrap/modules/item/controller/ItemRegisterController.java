package com.ddng.adminuibootstrap.modules.item.controller;

import com.ddng.adminuibootstrap.modules.common.clients.SaleClient;
import com.ddng.adminuibootstrap.modules.common.dto.sale.ItemTypeDto;
import com.ddng.adminuibootstrap.modules.common.utils.AlertUtils;
import com.ddng.adminuibootstrap.modules.item.form.RegisterForm;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
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
public class ItemRegisterController {

    private final SaleClient saleClient;

    /**
     * 상품 등록 메뉴 폼 요청
     */
    @GetMapping("/register-form")
    public String registerForm(Model model) {
        List<ItemTypeDto> types = saleClient.getItemTypes();
        model.addAttribute("types", types);
        model.addAttribute("registerForm", new RegisterForm());
        return "item/register/main";
    }

    /**
     * 상픔 등록 요청
     *
     * @param registerForm       등록할 내용
     */
    @PostMapping("/items")
    public String registerAction(@Valid
                                 @ModelAttribute RegisterForm registerForm,
                                 Errors errors,
                                 RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            AlertUtils.alertFail(redirectAttributes, errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("\n")));
            return "customer/register/main";
        }

        ResponseEntity responseEntity = saleClient.createItem(registerForm);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            AlertUtils.alertSuccess(redirectAttributes, registerForm.getName() + " 상품이 정상적으로 생성되었습니다.");
        } else {
            AlertUtils.alertFail(redirectAttributes, registerForm.getName() + " 상품을 등록하지 못했습니다.");
        }
        return "redirect:/item-management/item-register/register-form";
    }
}

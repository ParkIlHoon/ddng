package com.ddng.adminuibootstrap.modules.item.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <h1>상품 관리 메뉴 컨트롤러</h1>
 */
@Controller
@RequestMapping("/item")
public class ItemController
{
    /**
     * 상품 조회 메뉴 폼 요청
     * @param model
     * @return
     */
    @GetMapping("/search")
    public String searchForm (Model model)
    {
        return "item/search/main";
    }

    /**
     * 상품 등록 메뉴 폼 요청
     * @param model
     * @return
     */
    @GetMapping("/register")
    public String registerForm (Model model)
    {
        return "item/register/main";
    }

    /**
     * 바코드 생성 메뉴 폼 요청
     * @param model
     * @return
     */
    @GetMapping("/barcode")
    public String barcodeForm (Model model)
    {
        return "item/barcode/main";
    }
}

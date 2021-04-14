package com.ddng.adminuibootstrap.modules.item.controller;

import com.ddng.adminuibootstrap.modules.common.clients.SaleClient;
import com.ddng.adminuibootstrap.modules.item.template.ItemTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * <h1>상품 관리 > 바코드 생성 메뉴 컨트롤러</h1>
 */
@Controller
@RequestMapping("/item-management")
@RequiredArgsConstructor
public class BarcodeController
{
    private final SaleClient saleClient;

    /**
     * 바코드 생성 메뉴 폼 요청
     * @param model
     * @return
     */
    @GetMapping("/barcode-generator/barcode-form")
    public String barcodeForm (Model model)
    {
        List<String> barcodes = saleClient.getBarcodes(12);
        model.addAttribute("barcodes", barcodes);
        return "item/barcode/main";
    }
}

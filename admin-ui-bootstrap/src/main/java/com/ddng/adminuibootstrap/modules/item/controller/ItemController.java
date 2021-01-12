package com.ddng.adminuibootstrap.modules.item.controller;

import com.ddng.adminuibootstrap.modules.common.dto.RestPageImpl;
import com.ddng.adminuibootstrap.modules.common.dto.customer.SaleItemDto;
import com.ddng.adminuibootstrap.modules.common.dto.sale.ItemDto;
import com.ddng.adminuibootstrap.modules.common.dto.sale.ItemTypeDto;
import com.ddng.adminuibootstrap.modules.item.form.EditForm;
import com.ddng.adminuibootstrap.modules.item.template.ItemTemplate;
import com.ddng.adminuibootstrap.modules.sale.template.SaleTemplate;
import lombok.RequiredArgsConstructor;
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
 * <h1>상품 관리 > 상품 조회 메뉴 컨트롤러</h1>
 */
@Controller
@RequestMapping("/item-management/search-item")
@RequiredArgsConstructor
public class ItemController
{
    private final ItemTemplate itemTemplate;
    private final SaleTemplate saleTemplate;

    /**
     * 상품 조회 메뉴 폼 요청
     * @param model
     * @return
     */
    @GetMapping("/search-form")
    public String searchForm (Model model)
    {
        return "item/search/main";
    }

    /**
     * 상품 목록 조회 요청
     * @param keyword 검색 키워드
     * @param page 페이지
     * @param size 페이지당 건수
     * @param model
     * @return
     */
    @GetMapping("/item-list")
    public String searchAction (String keyword, int page, int size, Model model)
    {
        List<ItemDto> items = new ArrayList<>();
        long totalElements = 0;

        if (StringUtils.hasText(keyword))
        {
            RestPageImpl<ItemDto> restPage = itemTemplate.searchItemsWithPage(keyword, page, size);
            items = restPage.getContent();
            totalElements = restPage.getTotalElements();
        }

        model.addAttribute("items", items);
        model.addAttribute("totalElements", totalElements);
        return "item/search/main :: #item-list";
    }

    /**
     * 상품 조회 요청
     * @param id 조회할 상품의 아이디
     * @param model
     * @return
     */
    @GetMapping("/item-card/{id}")
    public String searchItemAction (@PathVariable("id") Long id, Model model)
    {
        if (id == null)
        {
            return "item/search/main :: #item-card";
        }

        ItemDto item = itemTemplate.getItem(id);
        List<ItemTypeDto> types = itemTemplate.getTypes();

        if (item != null)
        {
            //TODO 최다 구매 가족
            //TODO 결제 이력
            List<SaleItemDto> saleHistoryByItemId = saleTemplate.getSaleHistoryByItemId(item.getId());
            model.addAttribute("saleHistory", saleHistoryByItemId);
        }

        model.addAttribute("types", types);
        model.addAttribute("itemForm", new EditForm(item));
        return "item/search/main :: #item-card";
    }

    /**
     * 상품 수정 요청
     * @param id 수정할 상품의 아이디
     * @param editForm 수정할 내용
     * @param errors
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/items/{id}")
    public String editItemAction (@PathVariable("id") Long id,
                                  @Valid @ModelAttribute EditForm editForm,
                                  Errors errors,
                                  RedirectAttributes redirectAttributes)
    {
        if (errors.hasErrors())
        {
            return "item/search/main::#item-card";
        }

        itemTemplate.updateItem(id, editForm);
        redirectAttributes.addFlashAttribute("alertType", "success");
        redirectAttributes.addFlashAttribute("message", editForm.getName() + " 상품 정보가 정상적으로 변경되었습니다.");
        return "redirect:/item/search";
    }


}

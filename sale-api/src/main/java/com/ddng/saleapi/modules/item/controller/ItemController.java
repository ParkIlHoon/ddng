package com.ddng.saleapi.modules.item.controller;

import com.ddng.saleapi.modules.item.dto.ItemDto;
import com.ddng.saleapi.modules.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h1>상품 관련 요청 처리 컨트롤러</h1>
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController
{
    private final ItemService itemService;

    @GetMapping
    public ResponseEntity searchItem (String keyword,
                                      @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable)
    {
        Page<ItemDto.Response> items = itemService.searchByKeyword(keyword, pageable);
        return ResponseEntity.ok(items);
    }



}

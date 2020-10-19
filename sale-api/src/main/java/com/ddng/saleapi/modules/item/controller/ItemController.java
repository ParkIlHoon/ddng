package com.ddng.saleapi.modules.item.controller;

import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.item.dto.ItemDto;
import com.ddng.saleapi.modules.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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

    /**
     * 상품을 검색한다.
     * @param keyword 검색할 키워드
     * @param pageable 페이징 정보
     * @return 키워드에 해당하는 상품 목록
     */
    @GetMapping
    public ResponseEntity searchItem (String keyword,
                                      @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable)
    {
        Page<ItemDto.Response> items = itemService.searchByKeyword(keyword, pageable);
        return ResponseEntity.ok(items);
    }

    /**
     * 특정 상품을 조회한다.
     * @param id 조회할 상품 아이디
     * @return 아이디에 해당하는 상품 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity getItem (@PathVariable("id") Long id)
    {
        Optional<Item> optionalItem = itemService.findById(id);

        if (optionalItem.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }

        ItemDto.Response dto = new ItemDto.Response(optionalItem.get());
        return ResponseEntity.ok(dto);
    }



}

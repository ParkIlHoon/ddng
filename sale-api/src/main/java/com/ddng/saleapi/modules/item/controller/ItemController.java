package com.ddng.saleapi.modules.item.controller;

import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.item.dto.ItemDto;
import com.ddng.saleapi.modules.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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

    /**
     * 상품을 생성한다.
     * @param dto 생성할 상품 정보
     * @param errors
     * @return
     */
    @PostMapping
    public ResponseEntity createItem (@RequestBody @Valid ItemDto.Post dto, Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.badRequest().build();
        }

        Item created = itemService.createItem(dto);

        WebMvcLinkBuilder builder = linkTo(ItemController.class).slash(created.getId());
        return ResponseEntity.created(builder.toUri()).build();
    }

    /**
     * 상품 정보를 수정한다.
     * @param id 수정할 상품 아이디
     * @param dto 수정할 내용
     * @param errors
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity updateItem (@PathVariable("id") Long id,
                                      @RequestBody @Valid ItemDto.Put dto,
                                      Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.badRequest().build();
        }

        Optional<Item> optionalItem = itemService.findById(id);
        if (optionalItem.isEmpty())
        {
            return ResponseEntity.badRequest().build();
        }

        Item updated = itemService.updateItem(optionalItem.get(), dto);
        return ResponseEntity.ok(new ItemDto.Response(updated));
    }

    /**
     * 상품을 삭제한다.
     * @param id 삭제할 상품 아이디
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteItem (@PathVariable("id") Long id)
    {
        Optional<Item> optionalItem = itemService.findById(id);
        if (optionalItem.isEmpty())
        {
            return ResponseEntity.badRequest().build();
        }

        itemService.deleteItem(optionalItem.get());
        return ResponseEntity.ok().build();
    }
}

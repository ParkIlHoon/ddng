package com.ddng.saleapi.modules.item.controller;

import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.item.domain.ItemType;
import com.ddng.saleapi.modules.item.dto.ItemDto;
import com.ddng.saleapi.modules.item.service.ItemService;
import com.ddng.saleapi.modules.sale.dto.SaleDto;
import com.ddng.saleapi.modules.sale.service.SaleService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private final SaleService saleService;

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
     * 상품 종류를 취득한다.
     * @return
     */
    @GetMapping("/type")
    public ResponseEntity getItemTypes ()
    {
        List returnList = new ArrayList();
        ItemType[] values = ItemType.values();

        for(ItemType type : values)
        {
            HashMap row = new HashMap();
            row.put("id", type.name());
            row.put("text", type.getName());

            returnList.add(row);
        }

        return ResponseEntity.ok(returnList);
    }

    /**
     * 미용 카테고리의 상품을 검색한다.
     * @param keyword 검색 키워드
     * @param pageable 페이징 정보
     * @return
     */
    @GetMapping("/beauty")
    public ResponseEntity getBeautyItems (String keyword,
                                          @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable)
    {
        Page<ItemDto.Response> items = itemService.searchBeautyItemsByKeyword(keyword, pageable);
        return ResponseEntity.ok(items);
    }

    /**
     * 호텔 카테고리의 상품을 검색한다.
     * @return
     */
    @GetMapping("/hotel")
    public ResponseEntity getHotelItems ()
    {
        List<ItemDto.Response> items = itemService.findHotelItems();
        return ResponseEntity.ok(items);
    }

    /**
     * 유치원 카테고리의 상품을 검색한다.
     * @return
     */
    @GetMapping("/kindergarten")
    public ResponseEntity getKindergartenItems ()
    {
        List<ItemDto.Response> items = itemService.findKindergartenItems();
        return ResponseEntity.ok(items);
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

    /**
     * 상품의 최근 판매 기록을 조회한다.
     * @param id
     * @param pageable
     * @return
     */
    @GetMapping("/{id}/sale-history")
    public ResponseEntity getSaleHistory (@PathVariable("id") Long id,
                                          @PageableDefault(size = 10, sort = "saleDate", direction = Sort.Direction.DESC) Pageable pageable)
    {
        Optional<Item> optionalItem = itemService.findById(id);
        if (optionalItem.isEmpty())
        {
            return ResponseEntity.badRequest().build();
        }

        SaleDto.Get dto = new SaleDto.Get();
        dto.setOnlyToday(false);
        dto.setItem(optionalItem.get());

        Page<SaleDto.Response> responses = saleService.searchByDto(dto, pageable);
        return ResponseEntity.ok(responses);
    }

    /**
     * 사용 가능한 바코드 목록 조회
     * @param count 조회할 바코드 개수
     * @return
     */
    @GetMapping("/barcode")
    public ResponseEntity getBarcodeList (int count)
    {
        List<String> barcodes = itemService.getAvailableBarcodes(count);
        return ResponseEntity.ok(barcodes);
    }
}

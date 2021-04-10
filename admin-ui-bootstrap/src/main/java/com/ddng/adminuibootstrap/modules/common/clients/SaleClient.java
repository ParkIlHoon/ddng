package com.ddng.adminuibootstrap.modules.common.clients;

import com.ddng.adminuibootstrap.modules.common.dto.FeignPageImpl;
import com.ddng.adminuibootstrap.modules.common.dto.sale.ItemDto;
import com.ddng.adminuibootstrap.modules.common.dto.sale.ItemTypeDto;
import com.ddng.adminuibootstrap.modules.item.form.EditForm;
import com.ddng.adminuibootstrap.modules.item.form.RegisterForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <h1>Sale api Feign Client</h1>
 *
 * Spring Cloud Feign 기능을 사용해 RestTemplate 코드를 작성하지 않고 요청 기능을 구현한다.
 */
@FeignClient(name = "ddng-sale-api")
public interface SaleClient
{
    String ITEM_API = "/items";
    String SALE_API = "/sales";

    /**
     * 상품을 조회하는 API를 호출한다.
     * @param itemId 조회할 상품의 아이디
     * @return 아이디에 해당하는 상품
     */
    @GetMapping(ITEM_API + "/{itemId}")
    ItemDto getItem(@PathVariable("itemId") Long itemId);

    /**
     * 호텔 전체 상품을 조회하는 API를 호훌한다.
     * @return 호텔 전체 상품 목록
     */
    @GetMapping(ITEM_API + "/hotel")
    List<ItemDto> getHotelItems();

    /**
     * 유치원 전체 상품을 조회하는 API를 호출한다.
     * @return 유치원 전체 상품 목록
     */
    @GetMapping(ITEM_API + "/kindergarten")
    List<ItemDto> getKindergartenItems();

    /**
     * 상품 목록을 페이징해 검색하는 API를 호출한다.
     * @param keyword 조회할 키워드
     * @param page 조회할 페이지
     * @param size 조회할 건수
     * @return 상품 목록
     */
    @GetMapping(ITEM_API)
    FeignPageImpl<ItemDto> searchItemsWithPage(@RequestParam("keyword") String keyword,
                                               @RequestParam("page") int page,
                                               @RequestParam("size") int size);

    /**
     * 상품 종류 목록을 조회하는 API를 호출한다.
     * @return 상품 종류 목록
     */
    @GetMapping(ITEM_API + "/types")
    List<ItemTypeDto> getItemTypes();

    /**
     * 상품 정보를 수정하는 API를 호출한다.
     * @param itemId 수정할 상품의 아이디
     * @param editForm 수정할 정보
     */
    @PutMapping(ITEM_API + "/{itemId}")
    void updateItem(@PathVariable("itemId") Long itemId, EditForm editForm);

    /**
     * 상품을 생성하는 API를 호출한다.
     * @param registerForm 생성할 상품의 정보
     */
    @PostMapping(ITEM_API)
    void createItem(RegisterForm registerForm);

    /**
     * 바코드를 생성하는 API를 호출한다.
     * @param count 생성할 개수
     * @return 요청 개수만큼의 새로운 바코드 목록
     */
    @GetMapping(ITEM_API + "/barcode")
    List<String> getBarcodes(@RequestParam("count") int count);
}

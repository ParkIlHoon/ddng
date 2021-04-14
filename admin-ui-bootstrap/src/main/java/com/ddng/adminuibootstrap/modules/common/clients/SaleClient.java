package com.ddng.adminuibootstrap.modules.common.clients;

import com.ddng.adminuibootstrap.modules.common.dto.FeignPageImpl;
import com.ddng.adminuibootstrap.modules.common.dto.customer.NewCouponDto;
import com.ddng.adminuibootstrap.modules.common.dto.customer.SaleDto;
import com.ddng.adminuibootstrap.modules.common.dto.customer.SaleItemDto;
import com.ddng.adminuibootstrap.modules.common.dto.sale.*;
import com.ddng.adminuibootstrap.modules.item.form.EditForm;
import com.ddng.adminuibootstrap.modules.item.form.RegisterForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
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
    String SALE_API = "/sale";
    String COUPON_API = "/coupons";

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
     * 미용 상품을 페이징처리해 조회하는 API를 호출한다.
     * @param keyword 조회할 키워드
     * @param page 조회할 페이지
     * @param size 조회할 건수
     * @return 미용 상품 목록
     */
    @GetMapping(ITEM_API + "/beauty")
    FeignPageImpl<ItemDto> getBeautyItemsWithPage(@RequestParam("keyword") String keyword,
                                                  @RequestParam("page") int page,
                                                  @RequestParam("size") int size);

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

    /**
     * 쿠폰을 조회하는 API를 호출한다.
     * @param couponId 조회할 쿠폰 아이디
     * @return 아이디에 해당하는 쿠폰
     */
    @GetMapping(COUPON_API + "/{couponId}")
    CouponDto getCoupon(@PathVariable("couponId") Long couponId);

    /**
     * 사용자들이 가지고있는 쿠폰 목록을 조회하는 API를 호출한다.
     * @param customerIds 조회할 사용자 아이디 목록
     * @return 사용자들이 가지고있는 쿠폰 목록
     */
    @GetMapping(COUPON_API)
    FeignPageImpl<CouponDto> getCoupons(@RequestParam("customerIds") List<Long> customerIds);

    /**
     * 쿠폰 발급이 가능한 사용자 아이디 목록을 조회하는 API를 호출한다.
     * @return 쿠폰 발급이 가능한 사용자 아이디 목록
     */
    @GetMapping(COUPON_API + "/issuable")
    List<Long> getCouponIssuableCustomers();

    /**
     * 신규 쿠폰을 발급하는 API를 호출한다.
     * @param newCouponDto 새로 발급하는 쿠폰 정보
     * @return 상태코드
     */
    @PostMapping(COUPON_API)
    ResponseEntity<String> issueNewCoupons(NewCouponDto newCouponDto);

    /**
     * 새로운 판매를 생성하는 API를 호출한다.
     * @param newSaleDto 판매 정보
     * @return 상태 코드
     */
    @PostMapping(SALE_API)
    ResponseEntity<String> saleCart(NewSaleDto newSaleDto);

    /**
     * 가족의 구매 이력을 조회하는 API를 호출한다.
     * @param familyId 조회할 가족 아이디
     * @return 가족의 구매 이력록 목록
     */
    @GetMapping(SALE_API + "/history/family/{familyId}")
    List<SaleDto> getSaleHistoryByFamilyId(@PathVariable("familyId") Long familyId);

    /**
     * 고객의 구매 이력을 조회하는 API를 호출한다.
     * @param customerId 조회할 고객 아이디
     * @return 고객의 구매 이력 목록
     */
    @GetMapping(SALE_API + "/history/customer/{customerId}")
    List<SaleItemDto> getSaleHistoryByCustomerId(@PathVariable("customerId") Long customerId);

    /**
     * 상품의 판매 이력을 조회하는 API를 호출한다.
     * @param itemId 조회할 상품 아이디
     * @return 상품의 판매 이력 목록
     */
    @GetMapping(SALE_API + "/history/item/{itemId}")
    List<SaleItemDto> getSaleHistoryByItemId(@PathVariable("itemId") Long itemId);

    /**
     * 특정 기간 동안의 판매 기록을 조회하는 API를 호출한다.
     * @param salePeriodStart 조회 기간의 시작일자
     * @param salePeriodEnd 조회 기간의 종료일자
     * @param saleType 판매 유형
     * @param page 조회할 페이지
     * @param size 조회할 건수
     * @return 특정 기간 동안의 판매 기록
     */
    @GetMapping(SALE_API)
    FeignPageImpl<SaleDto> searchSale(@RequestParam("salePeriodStart") String salePeriodStart,
                             @RequestParam("salePeriodEnd") String salePeriodEnd,
                             @RequestParam("saleType")SaleType saleType,
                             @RequestParam("page") int page,
                             @RequestParam("size") int size);

    /**
     * 판매 내역을 조회하는 API를 호출한다.
     * @param saleId 조회할 판매 아이디
     * @return 판매 내역
     */
    @GetMapping(SALE_API + "/{saleId}")
    com.ddng.adminuibootstrap.modules.common.dto.sale.SaleDto getSale(@PathVariable("saleId") Long saleId);

    /**
     * 판매 내역을 환불하는 API를 호출한다.
     * @param saleId 환불할 판매 아이디
     * @return 상태코드
     */
    @DeleteMapping(SALE_API + "/{saleId}")
    ResponseEntity<String> refundSale(@PathVariable("saleId") Long saleId);
}

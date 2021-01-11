package com.ddng.saleapi.modules.sale.controller;

import com.ddng.saleapi.modules.sale.domain.Sale;
import com.ddng.saleapi.modules.sale.dto.CalculateDto;
import com.ddng.saleapi.modules.sale.dto.SaleDto;
import com.ddng.saleapi.modules.sale.dto.SaleItemDto;
import com.ddng.saleapi.modules.sale.service.SaleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * <h1>판매 관련 요청 처리 컨트롤러</h1>
 *
 * @version 1.0
 */
@RestController
@Api(value = "판매 컨트롤러")
@RequestMapping("/sale")
@RequiredArgsConstructor
public class SaleController
{
    private final SaleService saleService;

    /**
     * 새로운 판매 정보를 입력한다.
     * @param dto 판매 정보
     * @param errors
     * @return
     */
    @PostMapping
    @ApiOperation(value = "판매", notes = "새로운 판매를 생성합니다.")
    public ResponseEntity createSale (@RequestBody @Valid SaleDto.Post dto,
                                   Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.badRequest().build();
        }

        Sale sale = saleService.createSale(dto);
        WebMvcLinkBuilder builder = linkTo(SaleController.class).slash(sale.getId());
        return ResponseEntity.created(builder.toUri()).build();
    }

    /**
     * 판매 내역을 검색한다.
     * @param dto 검색 정보
     * @param errors
     * @param pageable
     * @return
     */
    @GetMapping
    @ApiOperation(value = "판매 목록 검색", notes = "검색 정보에 해당하는 판매 목록을 검색합니다.")
    public ResponseEntity searchSale (@RequestBody @Valid SaleDto.Get dto,
                                      Errors errors,
                                      @PageableDefault(size = 10, sort = "saleDate", direction = Sort.Direction.DESC) Pageable pageable)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.badRequest().build();
        }

        Page<SaleDto.Response> sales = saleService.searchByDto(dto, pageable);
        return ResponseEntity.ok(sales);
    }

    /**
     * 판매 정보를 조회한다.
     * @param id 조회할 판매 아이디
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "판매 조회", notes = "특정 판매를 조회합니다.")
    public ResponseEntity getSale (@PathVariable("id") Long id)
    {
        Optional<Sale> optionalSale = saleService.findById(id);

        if (optionalSale.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }

        SaleDto.ResponseWithSaleItem dto = new SaleDto.ResponseWithSaleItem(optionalSale.get());
        return ResponseEntity.ok(dto);
    }

    /**
     * 판매 정보를 수정한다.
     * @param id 수정할 판매 아이디
     * @param dto 수정할 내용
     * @param errors
     * @return
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "판매 수정", notes = "특정 판매 정보를 수정합니다.")
    public ResponseEntity updateSale (@PathVariable("id") Long id,
                                      @RequestBody @Valid SaleDto.Put dto,
                                      Errors errors)
    {
        Optional<Sale> optionalSale = saleService.findById(id);

        if (optionalSale.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }

        Sale updated = saleService.updateSale(optionalSale.get(), dto);
        SaleDto.ResponseWithSaleItem response = new SaleDto.ResponseWithSaleItem(updated);
        return ResponseEntity.ok(response);
    }

    /**
     * 가족의 구매 이력을 조회한다.
     * @param familyId 조회할 가족 아이디
     * @return
     */
    @GetMapping("/history/family/{familyId}")
    @ApiOperation(value = "가족의 판매 이력 조회", notes = "특정 가족의 판매 이력을 조회합니다.")
    public ResponseEntity getHistoryByFamily (@PathVariable("familyId") Long familyId)
    {
        if (familyId == null)
        {
            return ResponseEntity.badRequest().build();
        }

        List<SaleDto.ResponseWithSaleItem> history = saleService.getHistoryByFamily(familyId);
        return ResponseEntity.ok(history);
    }

    /**
     * 고객의 구매 이력을 조회한다.
     * @param customerId 조회할 고객 아이디
     * @return
     */
    @GetMapping("/history/customer/{customerId}")
    @ApiOperation(value = "고객의 판매 이력 조회", notes = "특정 고객의 판매 이력을 조회합니다.")
    public ResponseEntity getHistoryByCustomer (@PathVariable("customerId") Long customerId)
    {
        if (customerId == null)
        {
            return ResponseEntity.badRequest().build();
        }

        List<SaleItemDto.Get> history = saleService.getHistoryByCustomer(customerId);
        return ResponseEntity.ok(history);
    }

    /**
     * 상품의 구매 이력을 조회한다.
     * @param itemId 조회할 상품의 아이디
     * @return
     */
    @GetMapping("/history/item/{itemId}")
    @ApiOperation(value = "상품의 판매 이력 조회", notes = "특정 상품의 판매 이력을 조회합니다.")
    public ResponseEntity getHistoryByItem (@PathVariable("itemId") Long itemId)
    {
        if (itemId == null)
        {
            return ResponseEntity.badRequest().build();
        }

        List<SaleItemDto.Get> history = saleService.getHistoryByItem(itemId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/calculate/itemType")
    @ApiOperation(value = "상품 타입별 정산", notes = "특정 일자의 상품 타입별 판매 이력을 조회합니다.")
    public ResponseEntity calculateByItem(String baseDate)
    {
        List<CalculateDto.ByItem> calculate = saleService.getCalculateByItem(LocalDate.parse(baseDate));
        return ResponseEntity.ok(calculate);
    }

    @GetMapping("/calculate/payment")
    @ApiOperation(value = "결제 수단별 정산", notes = "특정 일자의 결제 수단별 판매 이력을 조회합니다.")
    public ResponseEntity calculateByPayment(String baseDate)
    {
        List<CalculateDto.ByPayment> calculate = saleService.getCalculateByPayment(LocalDate.parse(baseDate));
        return ResponseEntity.ok(calculate);
    }
}

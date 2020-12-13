package com.ddng.saleapi.modules.sale.controller;

import com.ddng.saleapi.modules.sale.domain.Sale;
import com.ddng.saleapi.modules.sale.dto.CalculateDto;
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


    @GetMapping("/calculate/item")
    public ResponseEntity calculateByItem(String baseDate)
    {
        List<CalculateDto.ByItem> calculate = saleService.getCalculateByItem(LocalDate.parse(baseDate));
        return ResponseEntity.ok(calculate);
    }

    @GetMapping("/calculate/payment")
    public ResponseEntity calculateByPayment(String baseDate)
    {
        List<CalculateDto.ByPayment> calculate = saleService.getCalculateByPayment(LocalDate.parse(baseDate));
        return ResponseEntity.ok(calculate);
    }
}

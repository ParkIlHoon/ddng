package com.ddng.saleapi.modules.sale.controller;

import com.ddng.saleapi.modules.sale.domain.Sale;
import com.ddng.saleapi.modules.sale.dto.SaleDto;
import com.ddng.saleapi.modules.sale.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

        SaleDto.Get dto = new SaleDto.Get(optionalSale.get());
        return ResponseEntity.ok(dto);
    }
}

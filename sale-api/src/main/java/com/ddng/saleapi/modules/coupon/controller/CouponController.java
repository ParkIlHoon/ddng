package com.ddng.saleapi.modules.coupon.controller;

import com.ddng.saleapi.modules.coupon.domain.Coupon;
import com.ddng.saleapi.modules.coupon.dto.CouponDto;
import com.ddng.saleapi.modules.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * <h1>쿠폰 관련 요청 처리 컨트롤러 클래스</h1>
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController
{
    private final CouponService couponService;

    /**
     * 쿠폰 목록을 검색한다.
     * @param dto
     * @param errors
     * @param pageable
     * @return
     */
    @GetMapping
    public ResponseEntity searchCoupon (@RequestBody @Valid CouponDto.Get dto,
                                        Errors errors,
                                        @PageableDefault(size = 10, sort = "expireDate", direction = Sort.Direction.DESC) Pageable pageable)
    {
        //FIXME 수정 필요
        if (errors.hasErrors())
        {
            return ResponseEntity.badRequest().build();
        }

        Page<CouponDto.Response> content = couponService.searchByDto(dto, pageable);
        return ResponseEntity.ok(content);
    }

    /**
     * 쿠폰을 조회한다.
     * @param id 조회할 쿠폰 아이디
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity getCoupon (@PathVariable("id") Long id)
    {
        Optional<Coupon> optionalCoupon = couponService.findById(id);

        if (optionalCoupon.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }

        CouponDto.Response content = new CouponDto.Response(optionalCoupon.get());
        return ResponseEntity.ok(content);
    }
}

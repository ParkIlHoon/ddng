package com.ddng.saleapi.modules.coupon.controller;

import com.ddng.saleapi.modules.coupon.domain.Coupon;
import com.ddng.saleapi.modules.coupon.dto.CouponDto;
import com.ddng.saleapi.modules.coupon.service.CouponService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * <h1>쿠폰 관련 요청 처리 컨트롤러 클래스</h1>
 *
 * @version 1.0
 */
@RestController
@Api(value = "쿠폰 컨트롤러")
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController
{
    private final CouponService couponService;

    /**
     * 쿠폰 목록을 조회한다.
     * @param customerIds 조회할 사용자 아이디 배열
     * @param pageable 페이징 정보
     * @return
     */
    @GetMapping
    @ApiOperation(value = "쿠폰 목록 검색", notes = "사용자들의 보유 쿠폰 목록을 조회합니다.")
    public ResponseEntity searchCoupon (@ApiParam(value = "사용자 아이디 목록", required = true) @RequestParam("customerIds") List<Long> customerIds,
                                        @ApiParam(value = "페이징 정보", required = false) @PageableDefault(size = 10, sort = "expireDate", direction = Sort.Direction.DESC) Pageable pageable)
    {
        if (customerIds == null || customerIds.size() == 0)
        {
            return ResponseEntity.badRequest().build();
        }

        CouponDto.Get dto = new CouponDto.Get();
        dto.setCustomerIds(customerIds);

        Page<CouponDto.Response> content = couponService.searchByDto(dto, pageable);
        return ResponseEntity.ok(content);
    }

    /**
     * 쿠폰을 조회한다.
     * @param id 조회할 쿠폰 아이디
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "쿠폰 조회", notes = "특정 쿠폰을 조회합니다.")
    public ResponseEntity getCoupon (@ApiParam(value = "쿠폰 아이디", required = true, example = "1") @PathVariable("id") Long id)
    {
        Optional<Coupon> optionalCoupon = couponService.findById(id);

        if (optionalCoupon.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }

        CouponDto.Response content = new CouponDto.Response(optionalCoupon.get());
        return ResponseEntity.ok(content);
    }

    /**
     * 쿠폰 발급이 가능한 사용자 목록을 조회한다.
     * @return
     */
    @GetMapping("/issuable")
    public ResponseEntity getCouponIssuableCustomers()
    {
        List<Long> customerIds = couponService.getCouponIssuableCustomerIds();
        return ResponseEntity.ok(customerIds);
    }
}

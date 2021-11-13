package com.ddng.scheduleapi.modules.common.clients;

import com.ddng.scheduleapi.modules.common.dto.sale.SaleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <h1>Sale api Feign Client</h1>
 * <p>
 * Spring Cloud Feign 기능을 사용해 RestTemplate 코드를 작성하지 않고 요청 기능을 구현한다.
 */
@FeignClient(name = "ddng-sale-api")
public interface SaleClient {

    String ITEM_API = "/items";
    String SALE_API = "/sale";
    String COUPON_API = "/coupons";

    /**
     * 판매 내역을 조회하는 API를 호출한다.
     *
     * @param saleId 조회할 판매 아이디
     * @return 판매 내역
     */
    @GetMapping(SALE_API + "/{saleId}")
    SaleDto getSale(@PathVariable("saleId") Long saleId);
}

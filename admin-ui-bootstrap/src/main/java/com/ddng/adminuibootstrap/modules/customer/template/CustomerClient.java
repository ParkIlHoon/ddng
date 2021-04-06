package com.ddng.adminuibootstrap.modules.customer.template;

import com.ddng.adminuibootstrap.modules.common.dto.FeignPageImpl;
import com.ddng.adminuibootstrap.modules.common.dto.customer.CustomerTypeDto;
import com.ddng.adminuibootstrap.modules.common.dto.customer.FamilyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <h1>Customer api Feign Client</h1>
 *
 * Spring Cloud Feign 기능을 사용해 RestTemplate 코드를 작성하지 않고 요청 기능을 구현한다.
 */
@FeignClient(name = "ddng-customer-api")
public interface CustomerClient
{
    /**
     * 고객 종류를 조회하는 API를 호출한다.
     * @return 고객 종류 리스트
     */
    @GetMapping("/customers/types")
    List<CustomerTypeDto> getCustomerTypes();

    @GetMapping("/families")
    FeignPageImpl<FamilyDto> searchFamilies(@RequestParam("keyword") String keyword);
}

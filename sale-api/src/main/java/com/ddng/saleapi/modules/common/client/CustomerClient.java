package com.ddng.saleapi.modules.common.client;

import com.ddng.saleapi.modules.common.dto.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "ddng-customer-api")
public interface CustomerClient {

    String CUSTOMER_API = "/customers";

    /**
     * 고객을 조회하는 API를 호출한다.
     *
     * @param customerId 조회할 고객의 아이디
     * @return 아이디에 해당하는 고객
     */
    @GetMapping(CUSTOMER_API + "/{customerId}")
    Optional<CustomerDto> getCustomer(@PathVariable("customerId") Long customerId);
}

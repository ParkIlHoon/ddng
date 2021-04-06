package com.ddng.adminuibootstrap.modules.customer.template;

import com.ddng.adminuibootstrap.modules.common.dto.customer.CustomerTypeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * <h1>Customer api Feign Client</h1>
 *
 * Spring Cloud Feign 기능을 사용해 RestTemplate 코드를 작성하지 않고 요청 기능을 구현한다.
 */
@FeignClient(name = "ddng-customer-api")
public interface CustomerClient
{

    @GetMapping("/customers/types")
    List<CustomerTypeDto> getCustomerTypes();
}

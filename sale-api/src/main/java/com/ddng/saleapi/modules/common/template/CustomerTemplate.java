package com.ddng.saleapi.modules.common.template;

import com.ddng.saleapi.infra.properties.ServiceProperties;
import com.ddng.saleapi.modules.common.dto.CustomerDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

/**
 * <h1>Customer-api 고객 관련 요청 템플릿</h1>
 */
@Component
public class CustomerTemplate extends AbstractTemplate
{
    public CustomerTemplate(RestTemplate restTemplate, ServiceProperties serviceProperties) {
        super(restTemplate, serviceProperties);
    }

    /**
     * 고객을 조회한다.
     * @param customerId 조회할 고객 아이디
     * @return
     */
    public Optional<CustomerDto> getCustomer(Long customerId)
    {
        String apiPath = CUSTOMER_API_PATH + "/" + customerId;
        URI targetUrl= getCustomerApiUriBuilder()
                .path(apiPath)
                .build()
                .encode()
                .toUri();
        CustomerDto forObject = restTemplate.getForObject(targetUrl, CustomerDto.class);
        return Optional.of(forObject);
    }
}

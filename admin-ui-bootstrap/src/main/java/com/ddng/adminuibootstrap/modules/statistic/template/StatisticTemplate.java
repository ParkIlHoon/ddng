package com.ddng.adminuibootstrap.modules.statistic.template;

import com.ddng.adminuibootstrap.infra.properties.ServiceProperties;
import com.ddng.adminuibootstrap.modules.common.template.AbstractTemplate;
import com.ddng.adminuibootstrap.modules.common.dto.statistic.CalculateDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Component
public class StatisticTemplate extends AbstractTemplate
{
    public StatisticTemplate(RestTemplate restTemplate, ServiceProperties serviceProperties)
    {
        super(restTemplate, serviceProperties);
    }

    /**
     * 상품 종류별 정산 데이터를 조회한다.
     * @param baseDate
     * @return
     */
    public List<CalculateDto.ByItemType> getCalculateByItemType (String baseDate)
    {
        String apiPath = SALE_API_PATH + "/calculate/itemType";
        URI targetUrl= getSaleApiUriBuilder()
                .path(apiPath)
                .queryParam("baseDate", baseDate)
                .build()
                .encode()
                .toUri();
        ParameterizedTypeReference<List<CalculateDto.ByItemType>> typeReference = new ParameterizedTypeReference<>() { };
        ResponseEntity<List<CalculateDto.ByItemType>> exchange = restTemplate.exchange(targetUrl, HttpMethod.GET, null, typeReference);
        return exchange.getBody();
    }

    /**
     * 결제 수단별 정산 데이터를 조회한다.
     * @param baseDate
     * @return
     */
    public List<CalculateDto.ByPayment> getCalculateByPayment (String baseDate)
    {
        String apiPath = SALE_API_PATH + "/calculate/payment";
        URI targetUrl= getSaleApiUriBuilder()
                .path(apiPath)
                .queryParam("baseDate", baseDate)
                .build()
                .encode()
                .toUri();
        ParameterizedTypeReference<List<CalculateDto.ByPayment>> typeReference = new ParameterizedTypeReference<>() { };
        ResponseEntity<List<CalculateDto.ByPayment>> exchange = restTemplate.exchange(targetUrl, HttpMethod.GET, null, typeReference);
        return exchange.getBody();
    }

}

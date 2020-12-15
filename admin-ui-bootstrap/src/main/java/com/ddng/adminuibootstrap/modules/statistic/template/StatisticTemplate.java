package com.ddng.adminuibootstrap.modules.statistic.template;

import com.ddng.adminuibootstrap.infra.properties.ServiceProperties;
import com.ddng.adminuibootstrap.modules.statistic.dto.CalculateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatisticTemplate
{
    private static final String SALE_API_PATH = "/sale/calculate";

    private final RestTemplate restTemplate;
    private final ServiceProperties serviceProperties;

    /**
     * 상품 종류별 정산 데이터를 조회한다.
     * @param baseDate
     * @return
     */
    public List<CalculateDto.ByItemType> getCalculateByItemType (String baseDate)
    {
        String apiPath = SALE_API_PATH + "/itemType";
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getSale())
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
        String apiPath = SALE_API_PATH + "/payment";
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getSale())
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

package com.ddng.scheduleapi.template.sale;

import com.ddng.scheduleapi.infra.properties.ServiceProperties;
import com.ddng.scheduleapi.template.sale.dto.SaleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * <h1>Sale-api 스케쥴 관련 RestTemplate 클래스</h1>
 */
@Component
@RequiredArgsConstructor
public class SaleTemplate
{
    private static final String SALE_API_PATH = "/sale";

    private final RestTemplate restTemplate;
    private final ServiceProperties serviceProperties;

    public SaleDto getSale(Long id)
    {
        String apiPath = SALE_API_PATH + "/" + id;
        URI targetUrl = UriComponentsBuilder.fromUriString(serviceProperties.getSale())
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        SaleDto forObject = restTemplate.getForObject(targetUrl, SaleDto.class);
        return forObject;
    }
}

package com.ddng.adminuibootstrap.modules.item.template;

import com.ddng.adminuibootstrap.infra.properties.ServiceProperties;
import com.ddng.adminuibootstrap.modules.item.dto.ItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * <h1>Sale-api 아이템 관련 RestTemplate 클래스</h1>
 */
@Component
@RequiredArgsConstructor
public class ItemTemplate
{
    private static final String ITEM_API_PATH = "/item";

    private final RestTemplate restTemplate;
    private final ServiceProperties serviceProperties;

    /**
     * 상품을 조회한다.
     * @param id 조회할 상품의 아이디
     * @return 아이디에 해당하는 상품
     */
    public ItemDto getItem (Long id)
    {
        String apiPath = ITEM_API_PATH + "/" + id;
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getSale())
                .path(apiPath)
                .build()
                .encode()
                .toUri();
        ItemDto forObject = restTemplate.getForObject(targetUrl, ItemDto.class);

        return forObject;
    }

    /**
     * 호텔 상품을 조회한다.
     * @return
     */
    public List<ItemDto> getHotelItems ()
    {
        String apiPath = ITEM_API_PATH + "/hotel";
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getSale())
                .path(apiPath)
                .build()
                .encode()
                .toUri();
        ResponseEntity<List<ItemDto>> exchange = restTemplate.exchange(targetUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<ItemDto>>() {
        });

        return exchange.getBody();
    }

    /**
     * 유치원 상품을 조회한다.
     * @return
     */
    public List<ItemDto> getKindergartenItems ()
    {
        String apiPath = ITEM_API_PATH + "/kindergarten";
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getSale())
                .path(apiPath)
                .build()
                .encode()
                .toUri();
        ResponseEntity<List<ItemDto>> exchange = restTemplate.exchange(targetUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<ItemDto>>() {
        });

        return exchange.getBody();
    }
}

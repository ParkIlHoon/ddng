package com.ddng.adminuibootstrap.modules.item.template;

import com.ddng.adminuibootstrap.infra.RestPageImpl;
import com.ddng.adminuibootstrap.infra.properties.ServiceProperties;
import com.ddng.adminuibootstrap.modules.item.dto.ItemDto;
import com.ddng.adminuibootstrap.modules.item.dto.ItemTypeDto;
import com.ddng.adminuibootstrap.modules.item.form.EditForm;
import com.ddng.adminuibootstrap.modules.item.form.RegisterForm;
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

    /**
     * 상품 목록을 검색한다.
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    public RestPageImpl<ItemDto> searchItemsWithPage(String keyword, int page, int size) 
    {
        String apiPath = ITEM_API_PATH;
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getSale())
                .path(apiPath)
                .queryParam("keyword", keyword)
                .queryParam("page", page)
                .queryParam("size", size)
                .build()
                .encode()
                .toUri();

        ParameterizedTypeReference<RestPageImpl<ItemDto>> typeReference = new ParameterizedTypeReference<>() {};
        ResponseEntity<RestPageImpl<ItemDto>> exchange = restTemplate.exchange(targetUrl, HttpMethod.GET, null, typeReference);

        return exchange.getBody();
    }

    /**
     * 상품 종류 목록을 조회한다.
     * @return
     */
    public List<ItemTypeDto> getTypes()
    {
        String apiPath = ITEM_API_PATH + "/types";
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getSale())
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        ParameterizedTypeReference<List<ItemTypeDto>> typeReference = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<ItemTypeDto>> exchange = restTemplate.exchange(targetUrl, HttpMethod.GET, null, typeReference);

        return exchange.getBody();
    }

    /**
     * 상품 정보를 수정한다.
     * @param id 수정할 상품의 아이디
     * @param editForm 수정할 내용
     */
    public void updateItem(Long id, EditForm editForm)
    {
        String apiPath = ITEM_API_PATH + "/" + id;
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getSale())
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        restTemplate.put(targetUrl, editForm);
    }

    /**
     * 상품을 생성한다.
     * @param registerForm 생성할 상품 정보
     */
    public void createItem(RegisterForm registerForm)
    {
        String apiPath = ITEM_API_PATH;
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getSale())
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(targetUrl, registerForm, String.class);
    }
}

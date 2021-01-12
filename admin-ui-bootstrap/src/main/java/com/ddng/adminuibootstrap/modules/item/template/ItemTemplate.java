package com.ddng.adminuibootstrap.modules.item.template;

import com.ddng.adminuibootstrap.modules.common.dto.RestPageImpl;
import com.ddng.adminuibootstrap.infra.properties.ServiceProperties;
import com.ddng.adminuibootstrap.modules.common.template.AbstractTemplate;
import com.ddng.adminuibootstrap.modules.common.dto.sale.ItemDto;
import com.ddng.adminuibootstrap.modules.common.dto.sale.ItemTypeDto;
import com.ddng.adminuibootstrap.modules.item.form.EditForm;
import com.ddng.adminuibootstrap.modules.item.form.RegisterForm;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

/**
 * <h1>Sale-api 아이템 관련 RestTemplate 클래스</h1>
 */
@Component
public class ItemTemplate extends AbstractTemplate
{
    public ItemTemplate(RestTemplate restTemplate, ServiceProperties serviceProperties)
    {
        super(restTemplate, serviceProperties);
    }

    /**
     * 상품을 조회한다.
     * @param id 조회할 상품의 아이디
     * @return 아이디에 해당하는 상품
     */
    public ItemDto getItem (Long id)
    {
        String apiPath = ITEM_API_PATH + "/" + id;
        URI targetUrl= getSaleApiUriBuilder()
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
        URI targetUrl= getSaleApiUriBuilder()
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
        URI targetUrl= getSaleApiUriBuilder()
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
        URI targetUrl= getSaleApiUriBuilder()
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
        URI targetUrl= getSaleApiUriBuilder()
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
        URI targetUrl= getSaleApiUriBuilder()
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
        URI targetUrl= getSaleApiUriBuilder()
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(targetUrl, registerForm, String.class);
    }

    /**
     * 바코드를 생성한다.
     * @return
     */
    public List<String> getBarcodes(int count)
    {
        String apiPath = ITEM_API_PATH + "/barcode";
        URI targetUrl= getSaleApiUriBuilder()
                .path(apiPath)
                .queryParam("count", count)
                .build()
                .encode()
                .toUri();

        ParameterizedTypeReference<List<String>> typeReference = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<String>> exchange = restTemplate.exchange(targetUrl, HttpMethod.GET, null, typeReference);

        return exchange.getBody();
    }
}

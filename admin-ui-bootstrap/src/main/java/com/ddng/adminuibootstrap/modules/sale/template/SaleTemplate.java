package com.ddng.adminuibootstrap.modules.sale.template;

import com.ddng.adminuibootstrap.infra.RestPageImpl;
import com.ddng.adminuibootstrap.infra.properties.ServiceProperties;
import com.ddng.adminuibootstrap.modules.customer.dto.SaleItemDto;
import com.ddng.adminuibootstrap.modules.item.dto.ItemDto;
import com.ddng.adminuibootstrap.modules.sale.dto.CouponDto;
import com.ddng.adminuibootstrap.modules.sale.dto.PaymentType;
import com.ddng.adminuibootstrap.modules.sale.dto.SaleDto;
import com.ddng.adminuibootstrap.modules.sale.dto.SaleType;
import com.ddng.adminuibootstrap.modules.sale.vo.Cart;
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
 * <h1>Sale-api 스케쥴 관련 RestTemplate 클래스</h1>
 */
@Component
@RequiredArgsConstructor
public class SaleTemplate
{
    private static final String SALE_API_PATH = "/sale";
    private static final String COUPON_API_PATH = "/coupon";
    private static final String ITEM_API_PATH = "/items";

    private final RestTemplate restTemplate;
    private final ServiceProperties serviceProperties;

    /**
     * 쿠폰을 조회한다.
     * @param id 조회할 쿠폰의 아이디
     * @return 아이디에 해당하는 쿠폰
     */
    public CouponDto getCoupon(Long id)
    {
        String apiPath = COUPON_API_PATH + "/" + id;
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getSale())
                .path(apiPath)
                .build()
                .encode()
                .toUri();
        CouponDto forObject = restTemplate.getForObject(targetUrl, CouponDto.class);
        return forObject;
    }

    /**
     * 카트에 있는 상품을 판매한다.
     * @param cart
     * @param saleType
     * @param paymentType
     */
    public void saleCart(Cart cart, SaleType saleType, PaymentType paymentType)
    {
        String apiPath = SALE_API_PATH;
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getSale())
                .path(apiPath)
                .build()
                .encode()
                .toUri();
        SaleDto saleDto = new SaleDto(cart, saleType, paymentType);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(targetUrl, saleDto, String.class);
    }

    /**
     * 쿠폰 목록을 조회한다.
     * @param customerIds
     * @return
     */
    public RestPageImpl<CouponDto> getCoupons(List<Long> customerIds)
    {
        String apiPath = COUPON_API_PATH;
        UriComponentsBuilder path = UriComponentsBuilder.fromUriString(serviceProperties.getSale()).path(apiPath);
        customerIds.forEach(c -> path.queryParam("customerIds", c));
        URI targetUrl = path.build().encode().toUri();

        ParameterizedTypeReference<RestPageImpl<CouponDto>> typeReference = new ParameterizedTypeReference<>() {};
        ResponseEntity<RestPageImpl<CouponDto>> responseEntity = restTemplate.exchange(targetUrl, HttpMethod.GET, null, typeReference);
        return responseEntity.getBody();
    }

    /**
     * 미용 상품 목록을 조회한다.
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    public RestPageImpl<ItemDto> getBeautyItems(String keyword, int page, int size)
    {
        String apiPath = ITEM_API_PATH + "/beauty";
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
     * 가족의 구매 이력을 조회한다.
     * @param familyId 가족 아이디
     * @return
     */
    public List<com.ddng.adminuibootstrap.modules.customer.dto.SaleDto> getSaleHistoryByFamilyId(Long familyId)
    {
        String apiPath = SALE_API_PATH + "/history/family/" + familyId;
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getSale())
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        ParameterizedTypeReference<List<com.ddng.adminuibootstrap.modules.customer.dto.SaleDto>> typeReference = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<com.ddng.adminuibootstrap.modules.customer.dto.SaleDto>> exchange = restTemplate.exchange(targetUrl, HttpMethod.GET, null, typeReference);
        return exchange.getBody();
    }

    /**
     * 고객의 구매 이력을 조회힌다.
     * @param id
     * @return
     */
    public List<SaleItemDto> getSaleHistoryByCustomerId(Long id)
    {
        String apiPath = SALE_API_PATH + "/history/customer/" + id;
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getSale())
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        ParameterizedTypeReference<List<SaleItemDto>> typeReference = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<SaleItemDto>> exchange = restTemplate.exchange(targetUrl, HttpMethod.GET, null, typeReference);
        return exchange.getBody();
    }

    /**
     * 상품의 판매 이력을 조회한다.
     * @param id
     * @return
     */
    public List<SaleItemDto> getSaleHistoryByItemId(Long id)
    {
        String apiPath = SALE_API_PATH + "/history/item/" + id;
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getSale())
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        ParameterizedTypeReference<List<SaleItemDto>> typeReference = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<SaleItemDto>> exchange = restTemplate.exchange(targetUrl, HttpMethod.GET, null, typeReference);
        return exchange.getBody();
    }
}

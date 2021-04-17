package com.ddng.adminuibootstrap.modules.sale.template;

import com.ddng.adminuibootstrap.modules.common.dto.RestPageImpl;
import com.ddng.adminuibootstrap.infra.properties.ServiceProperties;
import com.ddng.adminuibootstrap.modules.common.dto.sale.NewCouponDto;
import com.ddng.adminuibootstrap.modules.common.dto.sale.*;
import com.ddng.adminuibootstrap.modules.common.template.AbstractTemplate;
import com.ddng.adminuibootstrap.modules.sale.form.NewCouponForm;
import com.ddng.adminuibootstrap.modules.sale.vo.Cart;
import org.codehaus.jettison.json.JSONException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <h1>Sale-api 스케쥴 관련 RestTemplate 클래스</h1>
 */
@Component
public class SaleTemplate extends AbstractTemplate
{
    public SaleTemplate(RestTemplate restTemplate, ServiceProperties serviceProperties)
    {
        super(restTemplate, serviceProperties);
    }

    /**
     * 쿠폰을 조회한다.
     * @param id 조회할 쿠폰의 아이디
     * @return 아이디에 해당하는 쿠폰
     */
    public CouponDto getCoupon(Long id)
    {
        String apiPath = COUPON_API_PATH + "/" + id;
        URI targetUrl= getSaleApiUriBuilder()
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
    public HttpStatus saleCart(Cart cart, SaleType saleType, PaymentType paymentType)
    {
        String apiPath = SALE_API_PATH;
        URI targetUrl= getSaleApiUriBuilder()
                .path(apiPath)
                .build()
                .encode()
                .toUri();
        NewSaleDto newSaleDto = new NewSaleDto(cart, saleType, paymentType);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(targetUrl, newSaleDto, String.class);
        return responseEntity.getStatusCode();
    }

    /**
     * 쿠폰 목록을 조회한다.
     * @param customerIds
     * @return
     */
    public RestPageImpl<CouponDto> getCoupons(List<Long> customerIds)
    {
        String apiPath = COUPON_API_PATH;
        UriComponentsBuilder path = getSaleApiUriBuilder().path(apiPath);
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
     * 가족의 구매 이력을 조회한다.
     * @param familyId 가족 아이디
     * @return
     */
    public List<SaleDto> getSaleHistoryByFamilyId(Long familyId)
    {
        String apiPath = SALE_API_PATH + "/history/family/" + familyId;
        URI targetUrl= getSaleApiUriBuilder()
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        ParameterizedTypeReference<List<SaleDto>> typeReference = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<SaleDto>> exchange = restTemplate.exchange(targetUrl, HttpMethod.GET, null, typeReference);
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
        URI targetUrl= getSaleApiUriBuilder()
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
        URI targetUrl= getSaleApiUriBuilder()
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        ParameterizedTypeReference<List<SaleItemDto>> typeReference = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<SaleItemDto>> exchange = restTemplate.exchange(targetUrl, HttpMethod.GET, null, typeReference);
        return exchange.getBody();
    }

    /**
     * 쿠폰 발금이 가능한 사용자 아이디 목록을 조회한다.
     * @return
     */
    public List<Long> getCouponIssuableCustomers()
    {
        String apiPath = COUPON_API_PATH + "/issuable";
        URI targetUrl= getSaleApiUriBuilder()
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        ParameterizedTypeReference<List<Long>> typeReference = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<Long>> exchange = restTemplate.exchange(targetUrl, HttpMethod.GET, null, typeReference);
        return exchange.getBody();
    }

    /**
     * 신규 쿠폰을 발급한다.
     * @param newCouponForms
     * @return
     */
    public HttpStatus issueNewCoupons(List<NewCouponForm> newCouponForms)
    {
        String apiPath = COUPON_API_PATH;
        URI targetUrl= getSaleApiUriBuilder()
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        ResponseEntity<String> responseEntity = null;
        HttpStatus statusCode = null;
        for (NewCouponForm form : newCouponForms)
        {
            responseEntity = restTemplate.postForEntity(targetUrl, new NewCouponDto(form), String.class);
            statusCode = responseEntity.getStatusCode();
            if(!statusCode.is2xxSuccessful())
            {
                return statusCode;
            }
        }

        return statusCode;
    }

    /**
     * 판매 기록을 조회한다.
     * @param salePeriodStart 검색할 기록의 판매 시작 일자
     * @param salePeriodEnd 검색할 기록의 판매 종료 일자
     * @return
     */
    public List<SaleDto> searchSales(LocalDateTime salePeriodStart, LocalDateTime salePeriodEnd, SaleType saleType) throws JSONException {
        String apiPath = SALE_API_PATH;
        URI targetUrl= getSaleApiUriBuilder()
                .path(apiPath)
                .queryParam("salePeriodStart", salePeriodStart.toString())
                .queryParam("salePeriodEnd", salePeriodEnd.toString())
                .queryParam("saleType", saleType)
                .queryParam("page", 0)
                .queryParam("size", 50)
                .build()
                .encode()
                .toUri();

        ParameterizedTypeReference<RestPageImpl<SaleDto>> typeReference = new ParameterizedTypeReference<>() {};
        ResponseEntity<RestPageImpl<SaleDto>> exchange = restTemplate.exchange(targetUrl, HttpMethod.GET, null, typeReference);

        return exchange.getBody().getContent();
    }

    /**
     * 판매 내역을 조회한다.
     * @param id 조회할 판매 내역 아이디
     * @return
     */
    public com.ddng.adminuibootstrap.modules.common.dto.sale.SaleDto getSaleHistoryById(Long id)
    {
        String apiPath = SALE_API_PATH + "/" + id;
        URI targetUrl = getSaleApiUriBuilder().path(apiPath).build().encode().toUri();

        return restTemplate.getForObject(targetUrl, com.ddng.adminuibootstrap.modules.common.dto.sale.SaleDto.class);
    }

    /**
     * 특정 판매 이력을 환불한다.
     * @param id 환불할 판매 내역 아이디
     * @return
     */
    public HttpStatus refundSale(Long id)
    {
        String apiPath = SALE_API_PATH + "/" + id;
        URI targetUrl = getSaleApiUriBuilder().path(apiPath).build().encode().toUri();

        ResponseEntity<ResponseEntity> exchange = restTemplate.exchange(targetUrl, HttpMethod.DELETE, null, ResponseEntity.class);
        return exchange.getStatusCode();
    }
}

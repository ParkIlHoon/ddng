package com.ddng.adminuibootstrap.modules.sale.template;

import com.ddng.adminuibootstrap.infra.RestPageImpl;
import com.ddng.adminuibootstrap.infra.properties.ServiceProperties;
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
}

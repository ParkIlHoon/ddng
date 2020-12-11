package com.ddng.adminuibootstrap.modules.sale.template;

import com.ddng.adminuibootstrap.infra.properties.ServiceProperties;
import com.ddng.adminuibootstrap.modules.sale.dto.CouponDto;
import com.ddng.adminuibootstrap.modules.sale.dto.PaymentType;
import com.ddng.adminuibootstrap.modules.sale.dto.SaleDto;
import com.ddng.adminuibootstrap.modules.sale.dto.SaleType;
import com.ddng.adminuibootstrap.modules.sale.vo.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private static final String COUPON_API_PATH = "/coupon";

    private final RestTemplate restTemplate;
    private final ServiceProperties serviceProperties;

    /**
     * 쿠폰을 조회한다.
     * @param id 조회할 쿠폰의 아이디
     * @return 아이디에 해당하는 쿠폰
     */
    public CouponDto getCounpon (Long id)
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

    public void saleCart (Cart cart, SaleType saleType, PaymentType paymentType)
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
}

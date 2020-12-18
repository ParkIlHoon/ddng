package com.ddng.adminuibootstrap.modules.customer.template;

import com.ddng.adminuibootstrap.infra.properties.ServiceProperties;
import com.ddng.adminuibootstrap.modules.customer.form.RegisterForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * <h1>Customer-api 고객 관련 요청 템플릿</h1>
 */
@Component
@RequiredArgsConstructor
public class CustomerTemplate
{
    private static final String CUSTOMER_API_PATH = "/customer";

    private final RestTemplate restTemplate;
    private final ServiceProperties serviceProperties;

    /**
     * 고객을 생성하는 API를 호출한다.
     * @param registerForm 생성할 사용자 정보
     * @throws Exception
     */
    public void createCustomer (RegisterForm registerForm) throws Exception
    {
        String apiPath = CUSTOMER_API_PATH;
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getCustomer())
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(targetUrl, registerForm, String.class);
        HttpStatus statusCode = responseEntity.getStatusCode();

        if(statusCode.is2xxSuccessful())
        {

        }
        else if(statusCode.is4xxClientError())
        {
            throw new IllegalArgumentException();
        }
        else
        {
            throw new Exception(responseEntity.getHeaders().toString());
        }
    }
}

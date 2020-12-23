package com.ddng.adminuibootstrap.modules.customer.template;

import com.ddng.adminuibootstrap.infra.RestPageImpl;
import com.ddng.adminuibootstrap.infra.properties.ServiceProperties;
import com.ddng.adminuibootstrap.modules.customer.dto.CustomerDto;
import com.ddng.adminuibootstrap.modules.customer.dto.CustomerTagDto;
import com.ddng.adminuibootstrap.modules.customer.dto.CustomerTypeDto;
import com.ddng.adminuibootstrap.modules.customer.dto.FamilyDto;
import com.ddng.adminuibootstrap.modules.customer.form.FamilySettingForm;
import com.ddng.adminuibootstrap.modules.customer.form.RegisterForm;
import lombok.RequiredArgsConstructor;
import net.minidev.json.parser.JSONParser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * <h1>Customer-api 고객 관련 요청 템플릿</h1>
 */
@Component
@RequiredArgsConstructor
public class CustomerTemplate
{
    private static final String CUSTOMER_API_PATH = "/customers";
    private static final String FAMILY_API_PATH = "/families";
    private static final String TAG_API_PATH = "/tags";

    private final RestTemplate restTemplate;
    private final ServiceProperties serviceProperties;

    /**
     * 고객 종류를 조회하는 API를 호출한다.
     * @return
     */
    public List<CustomerTypeDto> getCustomerTypes ()
    {
        String apiPath = CUSTOMER_API_PATH + "/types";
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getCustomer())
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        ResponseEntity<List<CustomerTypeDto>> exchange = restTemplate.exchange(targetUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<CustomerTypeDto>>() {});
        List<CustomerTypeDto> types = exchange.getBody();
        return types;
    }

    /**
     * 가족 목록을 조회한다.
     * @param keyword 조회 키워드
     * @return
     */
    public List<FamilyDto> searchFamilies(String keyword)
    {
        String apiPath = FAMILY_API_PATH;
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getCustomer())
                .path(apiPath)
                .queryParam("keyword", keyword)
                .build()
                .encode()
                .toUri();
        ParameterizedTypeReference<RestPageImpl<FamilyDto>> typeReference = new ParameterizedTypeReference<>() {};
        ResponseEntity<RestPageImpl<FamilyDto>> exchange = restTemplate.exchange(targetUrl, HttpMethod.GET, null, typeReference);
        return exchange.getBody().getContent();
    }

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

    /**
     * 가족 목록을 페이징 처리해 검색한다.
     * @param keyword 조회 키워드
     * @param page 조회 페이지
     * @param size 조회 건수
     * @return
     */
    public RestPageImpl<FamilyDto> searchFamiliesWithPage(String keyword, int page, int size)
    {
        String apiPath = FAMILY_API_PATH;
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getCustomer())
                .path(apiPath)
                .queryParam("keyword", keyword)
                .queryParam("page", page)
                .queryParam("size", size)
                .build()
                .encode()
                .toUri();
        ParameterizedTypeReference<RestPageImpl<FamilyDto>> typeReference = new ParameterizedTypeReference<>() {};
        ResponseEntity<RestPageImpl<FamilyDto>> exchange = restTemplate.exchange(targetUrl, HttpMethod.GET, null, typeReference);
        return exchange.getBody();
    }

    /**
     * 가족을 조회한다.
     * @param id 조회할 가족 아이디
     * @return
     */
    public FamilyDto getFamily(Long id)
    {
        String apiPath = FAMILY_API_PATH + "/" + id;
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getCustomer())
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        FamilyDto forObject = restTemplate.getForObject(targetUrl, FamilyDto.class);
        return forObject;
    }

    /**
     * 가족 설정을 수정한다.
     * @param familySettingForm 수정할 내용
     */
    public void updateFamilySetting (FamilySettingForm familySettingForm)
    {
        String apiPath = FAMILY_API_PATH + "/" + familySettingForm.getId();
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getCustomer())
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        restTemplate.put(targetUrl, familySettingForm);
    }

    /**
     * 고객 목록을 페이징 처리해 조회한다.
     * @param keyword 조회할 키워드
     * @param page 조회 페이지
     * @param size 조회 사이즈
     * @return
     */
    public RestPageImpl<CustomerDto> searchCustomersWithPage(String keyword, int page, int size)
    {
        String apiPath = CUSTOMER_API_PATH;
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getCustomer())
                .path(apiPath)
                .queryParam("keyword", keyword)
                .queryParam("page", page)
                .queryParam("size", size)
                .build()
                .encode()
                .toUri();
        ParameterizedTypeReference<RestPageImpl<CustomerDto>> typeReference = new ParameterizedTypeReference<>() {};
        ResponseEntity<RestPageImpl<CustomerDto>> exchange = restTemplate.exchange(targetUrl, HttpMethod.GET, null, typeReference);
        return exchange.getBody();
    }

    /**
     * 고객을 조회한다.
     * @param id 조회할 고객 아이디
     * @return
     */
    public CustomerDto getCustomer(Long id)
    {
        String apiPath = CUSTOMER_API_PATH + "/" + id;
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getCustomer())
                .path(apiPath)
                .build()
                .encode()
                .toUri();
        CustomerDto forObject = restTemplate.getForObject(targetUrl, CustomerDto.class);
        return forObject;
    }

    /**
     * 고객 태그 목록을 조회한다.
     * @return
     */
    public List<CustomerTagDto> getCustomerTags ()
    {
        String apiPath = TAG_API_PATH;
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getCustomer())
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        ParameterizedTypeReference<List<CustomerTagDto>> typeReference = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<CustomerTagDto>> exchange = restTemplate.exchange(targetUrl, HttpMethod.GET, null, typeReference);
        return exchange.getBody();
    }

    /**
     * 고객에 태그를 추가한다.
     * @param id 고객 아이디
     * @param dto 추가할 태그 dto
     * @throws JSONException
     */
    public void addCustomerTag(Long id, CustomerTagDto dto) throws JSONException
    {
        String apiPath = CUSTOMER_API_PATH + "/" + id + "/tag";
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getCustomer())
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", dto.getTitle());

        HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
        restTemplate.exchange(targetUrl, HttpMethod.POST, entity, ResponseEntity.class);
    }

    /**
     * 고객의 태그를 제거한다.
     * @param id 고객 아이디
     * @param dto 제거할 태그 dto
     * @throws JSONException
     */
    public void removeCustomerTag(Long id, CustomerTagDto dto) throws JSONException
    {
        String apiPath = CUSTOMER_API_PATH + "/" + id + "/tag";
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getCustomer())
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", dto.getTitle());

        HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
        restTemplate.exchange(targetUrl, HttpMethod.DELETE, entity, ResponseEntity.class);
    }
}

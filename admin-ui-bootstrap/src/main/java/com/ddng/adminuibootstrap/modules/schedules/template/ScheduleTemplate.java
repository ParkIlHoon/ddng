package com.ddng.adminuibootstrap.modules.schedules.template;

import com.ddng.adminuibootstrap.infra.properties.ServiceProperties;
import com.ddng.adminuibootstrap.modules.schedules.dto.ScheduleDto;
import com.ddng.adminuibootstrap.modules.schedules.dto.ScheduleTagDto;
import com.ddng.adminuibootstrap.modules.schedules.dto.ScheduleTypeDto;
import com.ddng.adminuibootstrap.modules.schedules.form.ScheduleForm;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * <h1>Schedule-api 스케쥴 관련 RestTemplate 클래스</h1>
 */
@Component
@RequiredArgsConstructor
public class ScheduleTemplate
{
    private static final String SCHEDULE_API_PATH = "/schedules";
    private static final String TAG_API_PATH = "/tags";

    private final RestTemplate restTemplate;
    private final ServiceProperties serviceProperties;

    /**
     * 스케쥴을 조회한다.
     * @param id 조회할 스케쥴 아이디
     * @return 아이디에 해당하는 스케쥴
     */
    public ScheduleDto getSchedule(Long id)
    {
        String apiPath = SCHEDULE_API_PATH + "/" + id;
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getSchedule())
                .path(apiPath)
                .build()
                .encode()
                .toUri();
        ScheduleDto forObject = restTemplate.getForObject(targetUrl, ScheduleDto.class);

        return forObject;
    }

    /**
     * 기간 내에 해당하는 스케쥴 목록을 조회한다.
     * @param startDate 조회 시작일자
     * @param endDate 조회 종료일자
     * @return
     */
    public List<ScheduleDto> getSchedule(String startDate, String endDate)
    {
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getSchedule())
                .path(SCHEDULE_API_PATH)
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .build()
                .encode()
                .toUri();

        ParameterizedTypeReference<List<ScheduleDto>> typeReference = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<ScheduleDto>> exchange = restTemplate.exchange(targetUrl, HttpMethod.GET, null, typeReference);

        return exchange.getBody();
    }

    /**
     * 스케쥴 타입 목록을 조회한다.
     * @return
     */
    public List<ScheduleTypeDto> getScheduleTypes()
    {
        String apiPath = SCHEDULE_API_PATH + "/types";
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getSchedule())
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        ParameterizedTypeReference<List<ScheduleTypeDto>> typeReference = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<ScheduleTypeDto>> exchange = restTemplate.exchange(targetUrl, HttpMethod.GET, null, typeReference);

        return exchange.getBody();
    }

    /**
     * 스케쥴 태그 목록을 조회한다.
     * @return
     */
    public List<ScheduleTagDto> getScheduleTags()
    {
        String apiPath = TAG_API_PATH;
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getSchedule())
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        ParameterizedTypeReference<List<ScheduleTagDto>> typeReference = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<ScheduleTagDto>> exchange = restTemplate.exchange(targetUrl, HttpMethod.GET, null, typeReference);
        return exchange.getBody();
    }

    /**
     * 스케쥴을 생성한다.
     * @param form
     * @throws Exception
     */
    public void createSchedule(ScheduleForm form) throws Exception
    {
        String apiPath = SCHEDULE_API_PATH;
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getSchedule())
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(targetUrl, form, String.class);
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
     * 스케쥴을 수정한다.
     * @param id 수정할 스케쥴 아이디
     * @param form 수정할 내용
     */
    public void updateSchedule(Long id, ScheduleForm form)
    {
        String apiPath = SCHEDULE_API_PATH + "/" + id;
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getSchedule())
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        restTemplate.put(targetUrl, form);
    }

    /**
     * 스케쥴을 삭제한다.
     * @param id 삭제할 스케쥴 아이디
     */
    public void deleteSchedule(Long id)
    {
        String apiPath = SCHEDULE_API_PATH + "/" + id;
        URI targetUrl= UriComponentsBuilder.fromUriString(serviceProperties.getSchedule())
                .path(apiPath)
                .build()
                .encode()
                .toUri();

        restTemplate.delete(targetUrl);
    }
}

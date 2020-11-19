package com.ddng.adminuibootstrap.modules.main.utils;

import com.ddng.adminuibootstrap.modules.main.dto.ScheduleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * <h1>Schedule-API RESTful 통신을 위한 컴포넌트</h1>
 */
@Component
public class ScheduleRestTemplateClient
{
    @Autowired
    private RestTemplate restTemplate;
    private static final String SCHEDULE_SERVER_NAME = "http://ddng-schedule-api";

    /**
     * 스케쥴 목록을 조회한다.
     * @param startDate 조회 시작일자
     * @param endDate 조회 종료일자
     * @return
     */
    public ScheduleDto getSchedule(String startDate, String endDate)
    {
        String apiPath = "/schedules";
        URI targetUrl= UriComponentsBuilder.fromUriString(SCHEDULE_SERVER_NAME)
                                            .path(apiPath)
                                            .queryParam("startDate", startDate)
                                            .queryParam("endDate", endDate)
                                            .queryParam("calendarType", "DAILY")
                                            .build()
                                            .encode()
                                            .toUri();
        List forObject = restTemplate.getForObject(targetUrl, List.class);

        if (forObject.size() > 0)
        {
            //TODO 스케쥴 정보 DTO 변환
            //TODO 사용자 정보 조회 처리
            
        }

        return null;
    }
}

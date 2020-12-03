package com.ddng.adminuibootstrap.modules.schedules.template;

import com.ddng.adminuibootstrap.infra.properties.ServiceProperties;
import com.ddng.adminuibootstrap.modules.schedules.dto.ScheduleDto;
import lombok.RequiredArgsConstructor;
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
        List forObject = restTemplate.getForObject(targetUrl, List.class);

        if (forObject.size() > 0)
        {
            //TODO 스케쥴 정보 DTO 변환
            //TODO 사용자 정보 조회 처리

        }

        return null;
    }
}

package com.ddng.adminuibootstrap.modules.schedules.template;

import com.ddng.adminuibootstrap.infra.properties.ServiceProperties;
import com.ddng.adminuibootstrap.modules.schedules.dto.ScheduleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

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
}

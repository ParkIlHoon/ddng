package com.ddng.adminuibootstrap.modules.schedules.template;

import com.ddng.adminuibootstrap.modules.schedules.dto.ScheduleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class ScheduleTemplate
{
    @Autowired
    private RestTemplate restTemplate;
    private static final String SCHEDULE_SERVER_NAME = "http://ddng-schedule-api";

    public ScheduleDto getSchedule(Long id)
    {
        String apiPath = "/schedules/" + id;
        URI targetUrl= UriComponentsBuilder.fromUriString(SCHEDULE_SERVER_NAME)
                .path(apiPath)
                .build()
                .encode()
                .toUri();
        ScheduleDto forObject = restTemplate.getForObject(targetUrl, ScheduleDto.class);

        return forObject;
    }
}

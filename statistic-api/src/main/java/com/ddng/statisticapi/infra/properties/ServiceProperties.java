package com.ddng.statisticapi.infra.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <h1>service properties 객체</h1>
 *
 * application.properties 의 service 설정을 객체화 하는 클래스
 *
 * @see com.ddng.scheduleapi.infra.config.AppConfig
 */
@Getter @Setter
@ConfigurationProperties("service")
public class ServiceProperties
{
    /**
     * customer-api 서버 URL
     */
    private String customer;
    /**
     * sale-api 서버 URL
     */
    private String sale;
    /**
     * schedule-api 서버 URL
     */
    private String schedule;
    /**
     * statistic-api 서버 URL
     */
    private String statistic;
    /**
     * user-api 서버 URL
     */
    private String user;
}

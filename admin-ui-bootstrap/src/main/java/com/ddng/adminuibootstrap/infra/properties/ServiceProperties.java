package com.ddng.adminuibootstrap.infra.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <h1>service properties 객체</h1>
 *
 * application.properties 의 service 설정을 객체화 하는 클래스
 *
 * @see com.ddng.adminuibootstrap.infra.config.AppConfig
 */
@Getter @Setter
@ConfigurationProperties("service")
public class ServiceProperties
{
    private String customer;
    private String sale;
    private String schedule;
    private String statistic;
    private String user;
}

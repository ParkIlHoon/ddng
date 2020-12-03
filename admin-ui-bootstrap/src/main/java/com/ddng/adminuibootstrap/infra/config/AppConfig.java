package com.ddng.adminuibootstrap.infra.config;

import com.ddng.adminuibootstrap.infra.properties.ServiceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * <h1>애플리케이션 설정 클래스</h1>
 *
 * 1. application.properties 의 service 설정을 ServiceProperties 클래스로 객체화함
 * 2. RestTemplate 빈 생성
 *
 * @see com.ddng.adminuibootstrap.infra.properties.ServiceProperties
 */
@Configuration
@EnableConfigurationProperties(ServiceProperties.class)
public class AppConfig
{
    /**
     * RestTemplate 빈 생성
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}

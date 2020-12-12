package com.ddng.scheduleapi.infra.config;

import com.ddng.scheduleapi.infra.properties.ServiceProperties;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(ServiceProperties.class)
public class AppConfig
{
    @Bean
    public ModelMapper modelMapper ()
    {
        return new ModelMapper();
    }

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

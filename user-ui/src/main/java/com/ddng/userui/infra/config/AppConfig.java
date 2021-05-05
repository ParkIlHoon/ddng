package com.ddng.userui.infra.config;

import com.ddng.userui.infra.properties.ServiceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ServiceProperties.class)
public class AppConfig {
}

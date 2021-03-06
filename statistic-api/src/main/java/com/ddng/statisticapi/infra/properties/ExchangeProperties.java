package com.ddng.statisticapi.infra.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <h1>exchange properties 객체</h1>
 *
 * application.properties 의 exchange 설정을 객체화 하는 클래스
 *
 * @see com.ddng.statisticapi.infra.config.RabbitMQConfig
 */
@Getter @Setter
@ConfigurationProperties("exchange")
public class ExchangeProperties
{
    /**
     * 결제 exchange 명
     */
    private String selling;
    /**
     * 결제 exchange route key
     */
    private String sellingRouteKey;

    private String queue;

    private String anyRouteKey;
}

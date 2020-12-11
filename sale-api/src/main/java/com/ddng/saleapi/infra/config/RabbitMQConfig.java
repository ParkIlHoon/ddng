package com.ddng.saleapi.infra.config;

import com.ddng.saleapi.infra.properties.ExchangeProperties;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <h1>RabbitMQ 설정 클래스</h1>
 *
 * 1. application.properties 의 exchange 설정을 ExchangeProperties 클래스로 객체화함
 * 2. 발행할 TopicExchange Bean 을 생성한다.
 *
 * @see com.ddng.saleapi.infra.properties.ExchangeProperties
 */
@Configuration
@EnableConfigurationProperties(ExchangeProperties.class)
public class RabbitMQConfig
{
    @Autowired
    private ExchangeProperties exchangeProperties;

    /**
     * 판매 이벤트를 보낼 TopicExchange 를 설정
     * @return
     */
    @Bean
    public TopicExchange sellingExchange ()
    {
        return new TopicExchange(exchangeProperties.getSelling());
    }

    /**
     * RabbitMQ 의 기본 직렬화 메커니즘을 JSON 으로 설정
     *
     * 1. 자바 직렬화는 헤더(__TypeId__)를 이용해 클래스 전체 이름에 태그를 지정한다.
     *    즉, 같은 클래스 명을 이용해 메시지를 역직렬화 하려면 구동자가 같은 패키지에 있어야한다.
     *    그렇게되면 서비스 간 강한 결합이 생긴다.
     * 2. 향후 다른 언어로 이뤄진 서비스와 연결하려면 자바 직렬화를 사용할 수 없음
     * 3. 읽기 쉬운 포맷으로 변환해 개발 생산성 및 에러 분석 고려
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate (final ConnectionFactory connectionFactory)
    {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    /**
     * Jackson2JsonMessageConverter 빈 생성
     * @return
     */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter ()
    {
        return new Jackson2JsonMessageConverter();
    }
}

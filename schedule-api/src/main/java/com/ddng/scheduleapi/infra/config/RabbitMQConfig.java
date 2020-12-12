package com.ddng.scheduleapi.infra.config;

import com.ddng.scheduleapi.infra.properties.ExchangeProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

/**
 * <h1>RabbitMQ 설정 클래스</h1>
 *
 * 1. application.properties 의 exchange 설정을 ExchangeProperties 클래스로 객체화함
 * 2. 발행할 TopicExchange Bean 을 생성한다.
 *
 * @see com.ddng.scheduleapi.infra.properties.ExchangeProperties
 */
@Configuration
@EnableConfigurationProperties(ExchangeProperties.class)
public class RabbitMQConfig implements RabbitListenerConfigurer
{
    @Autowired
    private ExchangeProperties exchangeProperties;

    /**
     * selling TopicExchange 설정
     * @return
     */
    @Bean
    public TopicExchange sellingExchange ()
    {
        return new TopicExchange(exchangeProperties.getSelling());
    }

    /**
     * Queue 설정
     * @return
     */
    @Bean
    public Queue sellingExchangeQueue ()
    {
        return new Queue(exchangeProperties.getQueue());
    }

    /**
     * Queue 와 TopicExchange 를 바인딩
     * @param queue
     * @param topicExchange
     * @return
     */
    @Bean
    public Binding binding(final Queue queue, final TopicExchange topicExchange)
    {
        return BindingBuilder.bind(queue).to(topicExchange).with(exchangeProperties.getAnyRouteKey());
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter()
    {
        return new MappingJackson2MessageConverter();
    }

    @Bean
    public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory()
    {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(consumerJackson2MessageConverter());
        return factory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar)
    {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }
}

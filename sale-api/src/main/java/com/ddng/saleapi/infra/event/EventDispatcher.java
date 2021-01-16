package com.ddng.saleapi.infra.event;

import com.ddng.saleapi.infra.properties.ExchangeProperties;
import com.ddng.saleapi.modules.sale.event.SellingEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventDispatcher
{
    private final RabbitTemplate rabbitTemplate;
    private final ExchangeProperties exchangeProperties;

    /**
     * 메시지큐로 판매 이벤트를 전달한다
     * @param sellingEvent 전달할 판매 이벤트
     */
    public void send(final SellingEvent sellingEvent)
    {
        rabbitTemplate.convertAndSend(exchangeProperties.getSelling(), exchangeProperties.getSellingRouteKey(), sellingEvent);
    }
}

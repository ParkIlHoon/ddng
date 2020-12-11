package com.ddng.scheduleapi.modules.schedules.event;

import com.ddng.scheduleapi.modules.schedules.service.SchedulesService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * <h1>결제 이벤트 핸들러</h1>
 *
 * @see com.ddng.scheduleapi.modules.schedules.event.SellingEvent
 */
@Component
@RequiredArgsConstructor
public class SellingEventHandler
{
    private final SchedulesService schedulesService;

    /**
     * 결제 이벤트를 받는다.
     * @param event
     */
    @RabbitListener(queues = "${exchange.queue}")
    public void handleEvent(final SellingEvent event)
    {
        Long saleId = event.getSaleId();
        System.out.println(saleId);
    }
}

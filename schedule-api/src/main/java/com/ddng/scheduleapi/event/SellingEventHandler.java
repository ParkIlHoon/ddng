package com.ddng.scheduleapi.event;

import com.ddng.scheduleapi.modules.schedules.service.SchedulesService;
import com.ddng.scheduleapi.template.sale.SaleTemplate;
import com.ddng.scheduleapi.template.sale.dto.SaleDto;
import com.ddng.scheduleapi.template.sale.dto.SaleItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <h1>결제 이벤트 핸들러</h1>
 *
 */
@Component
@RequiredArgsConstructor
public class SellingEventHandler
{
    private final SaleTemplate saleTemplate;
    private final SchedulesService schedulesService;

    /**
     * 결제 이벤트를 받는다.
     * @param event
     */
    @RabbitListener(queues = "${exchange.queue}")
    public void handleEvent(final SellingEvent event)
    {
        Long saleId = event.getSaleId();
        System.out.println("saleID : " + saleId);
        try
        {
            if (saleId != null)
            {
                SaleDto sale = saleTemplate.getSale(saleId);

                List<SaleItemDto> saleItems = sale.getSaleItemList();
                for(SaleItemDto saleItem : saleItems)
                {
                    Long scheduleId = saleItem.getScheduleId();
                    System.out.println("scheduleID : " + scheduleId);
                    if (scheduleId != null)
                    {
                        schedulesService.paySchedule(scheduleId);
                    }
                }
            }
        }
        catch (final Exception exception)
        {
            throw new AmqpRejectAndDontRequeueException(exception);
        }
    }
}

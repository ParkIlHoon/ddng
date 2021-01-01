package com.ddng.statisticapi.modules.sale.event;

import com.ddng.statisticapi.modules.sale.dto.SaleDto;
import com.ddng.statisticapi.modules.sale.template.SaleTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * <h1>결제 이벤트 핸들러</h1>
 *
 * @see com.ddng.statisticapi.modules.sale.event.SellingEvent
 */
@Component
@RequiredArgsConstructor
public class SellingEventHandler
{
    private final SaleTemplate saleTemplate;

    /**
     * 결제 이벤트를 받는다.
     * @param event
     */
    @RabbitListener(queues = "${exchange.queue}")
    public void handleEvent(final SellingEvent event)
    {
        Long saleId = event.getSaleId();
        System.out.println(saleId);

        if(saleId != null)
        {
            SaleDto sale = saleTemplate.getSale(saleId);

        }
    }
}

package com.ddng.scheduleapi.event;

import com.ddng.scheduleapi.modules.common.clients.SaleClient;
import com.ddng.scheduleapi.modules.common.dto.sale.SaleDto;
import com.ddng.scheduleapi.modules.common.dto.sale.SaleItemDto;
import com.ddng.scheduleapi.modules.schedules.service.SchedulesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <h1>결제 이벤트 핸들러</h1>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SellingEventHandler {

    private final SaleClient saleClient;
    private final SchedulesService schedulesService;

    /**
     * 결제 이벤트를 받는다.
     *
     * @param event
     */
    @RabbitListener(queues = "${exchange.queue}")
    public void handleEvent(final SellingEvent event) {
        Long saleId = event.getSaleId();
        log.info("saleID : " + saleId);
        try {
            if (saleId != null) {
                SaleDto sale = saleClient.getSale(saleId);

                List<SaleItemDto> saleItems = sale.getSaleItemList();
                for (SaleItemDto saleItem : saleItems) {
                    Long scheduleId = saleItem.getScheduleId();
                    log.info("scheduleID : " + scheduleId);
                    if (scheduleId != null) {
                        schedulesService.paySchedule(scheduleId);
                    }
                }
            }
        } catch (final Exception exception) {
            throw new AmqpRejectAndDontRequeueException(exception);
        }
    }
}

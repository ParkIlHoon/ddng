package com.ddng.scheduleapi.modules.schedules.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.io.Serializable;

/**
 * <h1>결제 이벤트</h1>
 *
 */
@Getter
@RequiredArgsConstructor
@ToString @EqualsAndHashCode
@JsonDeserialize(using = SellingEventDeserializer.class)
public class SellingEvent implements Serializable
{
    private final Long saleId;
}

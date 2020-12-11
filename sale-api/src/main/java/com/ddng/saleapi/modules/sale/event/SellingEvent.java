package com.ddng.saleapi.modules.sale.event;

import lombok.*;

import java.io.Serializable;

/**
 * <h1>결제 이벤트</h1>
 *
 */
@Getter
@RequiredArgsConstructor
@ToString @EqualsAndHashCode
public class SellingEvent implements Serializable
{
    private final Long saleId;
}

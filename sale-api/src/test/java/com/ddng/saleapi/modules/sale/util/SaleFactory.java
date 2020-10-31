package com.ddng.saleapi.modules.sale.util;

import com.ddng.saleapi.modules.sale.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h1>판매 생성 팩토리</h1>
 */
@Component
@Transactional
@RequiredArgsConstructor
public class SaleFactory
{
    private final SaleRepository saleRepository;
}

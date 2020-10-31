package com.ddng.saleapi.modules.sale.repository;

import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.sale.domain.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface SaleCustomRepository
{
    Page<Sale> findSaleByItem(Item item, Pageable pageable);
}

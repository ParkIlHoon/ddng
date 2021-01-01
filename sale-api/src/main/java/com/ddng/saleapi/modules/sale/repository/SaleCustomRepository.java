package com.ddng.saleapi.modules.sale.repository;

import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.sale.domain.Sale;
import com.ddng.saleapi.modules.sale.domain.SaleItem;
import com.ddng.saleapi.modules.sale.dto.CalculateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface SaleCustomRepository
{
    Page<Sale> findSaleByItem(Item item, Pageable pageable);

    List<Sale> findSaleByFamilyId(Long familyId);
    List<SaleItem> findSaleByCustomerId(Long customerId);

    List<CalculateDto.ByItem> calculateByItem (LocalDate date);
    List<CalculateDto.ByPayment> calculateByPayment (LocalDate date);
}

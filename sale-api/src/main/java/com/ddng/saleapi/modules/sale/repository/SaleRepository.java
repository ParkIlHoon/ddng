package com.ddng.saleapi.modules.sale.repository;

import com.ddng.saleapi.modules.sale.domain.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <h1>판매 리포지토리 인터페이스</h1>
 *
 * @version 1.0
 */
@Transactional(readOnly = true)
public interface SaleRepository extends JpaRepository<Sale, Long>
{
    Page<Sale> findBySaleDateAfterAndSaleDateBefore(LocalDateTime sttDate, LocalDateTime endDate, Pageable pageable);
}

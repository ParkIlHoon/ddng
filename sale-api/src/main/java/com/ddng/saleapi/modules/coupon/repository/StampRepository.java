package com.ddng.saleapi.modules.coupon.repository;

import com.ddng.saleapi.modules.coupon.domain.Stamp;
import com.ddng.saleapi.modules.item.domain.ItemType;
import com.ddng.saleapi.modules.sale.domain.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface StampRepository extends JpaRepository<Stamp, Long>, StampCustomRepository
{
    Optional<Stamp> findByCustomerId(Long customerId);
    Optional<Stamp> findByCustomerIdAndItemType(Long customerId, ItemType itemType);
    List<Stamp> findAllBySale(Sale sale);
}

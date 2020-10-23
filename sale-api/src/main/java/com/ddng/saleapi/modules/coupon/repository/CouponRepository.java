package com.ddng.saleapi.modules.coupon.repository;

import com.ddng.saleapi.modules.coupon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CouponRepository extends JpaRepository<Coupon, Long>
{
    List<Coupon> findByIdInAndUseDateIsNullAndExpireDateAfter(List<Long> ids, LocalDateTime dateTime);
}

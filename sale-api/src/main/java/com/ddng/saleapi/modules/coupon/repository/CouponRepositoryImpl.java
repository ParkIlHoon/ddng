package com.ddng.saleapi.modules.coupon.repository;

import com.ddng.saleapi.modules.coupon.domain.QCoupon;
import com.ddng.saleapi.modules.coupon.dto.CouponDto;
import com.ddng.saleapi.modules.coupon.dto.QCouponDto_Response;
import com.ddng.saleapi.modules.item.domain.QItem;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.util.List;

import static com.ddng.saleapi.modules.coupon.domain.QCoupon.coupon;
import static com.ddng.saleapi.modules.item.domain.QItem.item;

public class CouponRepositoryImpl implements CouponCustomRepository
{
    private final JPAQueryFactory queryFactory;

    public CouponRepositoryImpl(EntityManager entityManager)
    {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<CouponDto.Response> searchCoupon(CouponDto.Get dto, Pageable pageable)
    {
        QueryResults<CouponDto.Response> queryResults =
                queryFactory.select(new QCouponDto_Response(coupon.id, coupon.customerId, coupon.name,
                                                            coupon.createDate, coupon.expireDate, coupon.useDate,
                                                            coupon.type, item.name))
                            .from(coupon)
                            .leftJoin(coupon.item, item)
                            .where(
                                    customerIdIn(dto.getCustomerIds())
                                    )
                            .offset(pageable.getOffset())
                            .limit(pageable.getPageSize())
                            .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }

    private BooleanExpression customerIdEq(Long customerId)
    {
        return customerId != null ? coupon.customerId.eq(customerId) : null;
    }
    private BooleanExpression customerIdIn(List<Long> customerIds)
    {
        return customerIds != null ? coupon.customerId.in(customerIds) : null;
    }
}

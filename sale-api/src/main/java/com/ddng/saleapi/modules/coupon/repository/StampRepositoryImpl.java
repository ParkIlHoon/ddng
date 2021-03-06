package com.ddng.saleapi.modules.coupon.repository;

import com.ddng.saleapi.modules.coupon.domain.Stamp;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static com.ddng.saleapi.modules.coupon.domain.QStamp.stamp;

public class StampRepositoryImpl implements StampCustomRepository
{
    private final JPAQueryFactory queryFactory;

    public StampRepositoryImpl(EntityManager entityManager)
    {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Long> getCouponIssuableCustomerIds(int standardCount)
    {
        QueryResults<Tuple> queryResults = queryFactory.select(stamp.customerId, stamp.count().as("count"))
                                                        .from(stamp)
                                                        .where(stamp.coupon.isNull().and(stamp.customerId.isNotNull()))
                                                        .groupBy(stamp.customerId)
                                                        .fetchResults();

        List<Long> collect = queryResults.getResults().stream()
                                        .filter(tuple -> tuple.get(stamp.count().as("count")) > standardCount)
                                        .map(tuple -> tuple.get(stamp.customerId))
                                        .collect(Collectors.toList());

        return collect;
    }

    @Override
    public List<Stamp> getStampsForIssueCoupon(Long customerId, int countOfIssueCoupon)
    {
       return queryFactory.selectFrom(stamp)
                    .where(stamp.coupon.isNull().and(stamp.customerId.eq(customerId)))
                    .limit(countOfIssueCoupon)
                    .orderBy(stamp.stampDate.asc())
                    .fetch();
    }
}

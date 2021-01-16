package com.ddng.saleapi.modules.coupon.repository;

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
    public List<Long> getCouponChangableCustomerIds(List<Long> customerIds, int standardCount)
    {
        QueryResults<Tuple> queryResults = queryFactory.select(stamp.customerId, stamp.count().as("count"))
                                                        .from(stamp)
                                                        .where(
                                                                stamp.customerId.in(customerIds)
                                                                        .and(stamp.coupon.isNull())
                                                        )
                                                        .groupBy(stamp.customerId)
                                                        .fetchResults();

        List<Long> collect = queryResults.getResults().stream()
                                        .filter(tuple -> tuple.get(stamp.count().as("count")) > standardCount)
                                        .map(tuple -> tuple.get(stamp.customerId))
                                        .collect(Collectors.toList());

        return collect;
    }
}

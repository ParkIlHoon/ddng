package com.ddng.saleapi.modules.sale.repository;

import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.sale.domain.Sale;
import com.ddng.saleapi.modules.sale.domain.SaleItem;
import com.ddng.saleapi.modules.sale.domain.SaleType;
import com.ddng.saleapi.modules.sale.dto.CalculateDto;
import com.ddng.saleapi.modules.sale.dto.QCalculateDto_ByItem;
import com.ddng.saleapi.modules.sale.dto.QCalculateDto_ByPayment;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.ddng.saleapi.modules.item.domain.QItem.item;
import static com.ddng.saleapi.modules.sale.domain.QSale.sale;
import static com.ddng.saleapi.modules.sale.domain.QSaleItem.saleItem;

public class SaleRepositoryImpl implements SaleCustomRepository
{
    private final JPAQueryFactory queryFactory;

    public SaleRepositoryImpl(EntityManager entityManager)
    {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Sale> findSaleByItem(Item searchItem, Pageable pageable)
    {
        QueryResults<Sale> saleQueryResults = queryFactory.select(sale)
                                                            .from(saleItem)
                                                            .leftJoin(saleItem.item, item)
                                                            .leftJoin(saleItem.sale, sale)
                                                            .where(
                                                                    item.eq(searchItem)
                                                                    )
                                                            .groupBy(sale.id)
                                                            .offset(pageable.getOffset())
                                                            .limit(pageable.getPageSize())
                                                            .fetchResults();

        return new PageImpl<>(saleQueryResults.getResults(), pageable, saleQueryResults.getTotal());
    }

    @Override
    public List<Sale> findSaleByFamilyId(Long familyId)
    {
        List<Sale> fetch = queryFactory.select(sale)
                                        .from(sale)
                                        .leftJoin(sale.saleItemList, saleItem)
                                        .where(sale.familyId.eq(familyId))
                                        .groupBy(sale.id)
                                        .fetch();
        return fetch;
    }

    @Override
    public List<SaleItem> findSaleByCustomerId(Long customerId)
    {
        List<SaleItem> fetch = queryFactory.select(saleItem)
                                        .from(saleItem)
                                        .where(saleItem.customerId.eq(customerId))
                                        .fetch();
        return fetch;
    }

    @Override
    public List<SaleItem> findSaleByItemId(Long itemId)
    {
        List<SaleItem> fetch = queryFactory.select(saleItem)
                .from(saleItem)
                .where(saleItem.item.id.eq(itemId))
                .fetch();
        return fetch;
    }

    @Override
    public List<CalculateDto.ByItem> calculateByItem(LocalDate date)
    {
        JPAQuery<CalculateDto.ByItem> jpaQuery = queryFactory.select(new QCalculateDto_ByItem(
                                                                    saleItem.item.type,
                                                                    saleItem.count.sum(),
                                                                    saleItem.totalPrice.sum()
                                                            ))
                                                        .from(sale)
                                                        .leftJoin(sale.saleItemList, saleItem)
                                                        .where(
                                                                sale.type.eq(SaleType.PAYED)
                                                                .and(sale.saleDate.goe(date.atTime(LocalTime.MIN))
                                                                        .and(sale.saleDate.loe(date.atTime(LocalTime.MAX))))
                                                        )
                                                        .groupBy(saleItem.item.type);

        return jpaQuery.fetch();
    }

    @Override
    public List<CalculateDto.ByPayment> calculateByPayment(LocalDate date)
    {
        JPAQuery<CalculateDto.ByPayment> jpaQuery =
                queryFactory.select(new QCalculateDto_ByPayment(
                                        sale.payment,
                                        sale.count().intValue(),
                                        ExpressionUtils.as(
                                            JPAExpressions
                                                .select(saleItem.totalPrice.sum())
                                                .from(saleItem)
                                                .where(saleItem.sale.eq(sale).and(saleItem.sale.payment.eq(sale.payment))),"amount"))
                                    )
                            .from(sale)
                            .where(
                                    sale.type.eq(SaleType.PAYED)
                                        .and(sale.saleDate.goe(date.atStartOfDay())
                                                .and(sale.saleDate.loe(date.atTime(LocalTime.MAX))))
                                  )
                            .groupBy(sale.payment);

        return jpaQuery.fetch();
    }


}

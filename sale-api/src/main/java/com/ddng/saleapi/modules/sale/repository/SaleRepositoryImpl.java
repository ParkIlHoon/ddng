package com.ddng.saleapi.modules.sale.repository;

import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.sale.domain.Sale;
import com.ddng.saleapi.modules.sale.dto.CalculateDto;
import com.ddng.saleapi.modules.sale.dto.QCalculateDto;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.time.LocalDate;
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
    public List<CalculateDto> calculate(LocalDate date)
    {
        JPAQuery<CalculateDto> jpaQuery = queryFactory.select(new QCalculateDto(
                                                                    saleItem.item.type,
                                                                    saleItem.count.sum(),
                                                                    saleItem.salePrice.multiply(saleItem.count).sum()
                                                            ))
                                                        .from(sale)
                                                        .leftJoin(sale.saleItemList, saleItem)
                                                        .where(
                                                                sale.saleDate.goe(date.atTime(LocalTime.MIN))
                                                                        .and(sale.saleDate.loe(date.atTime(LocalTime.MAX)))
                                                        )
                                                        .groupBy(saleItem.item.type);

        return jpaQuery.fetch();
    }
}

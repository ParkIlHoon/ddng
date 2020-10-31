package com.ddng.saleapi.modules.sale.repository;

import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.sale.domain.Sale;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

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
}

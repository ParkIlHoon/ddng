package com.ddng.saleapi.modules.item.repository;

import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.item.domain.ItemType;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import static com.ddng.saleapi.modules.item.domain.QItem.item;

public class ItemRepositoryImpl extends QuerydslRepositorySupport implements ItemCustomRepository
{
    public ItemRepositoryImpl()
    {
        super(Item.class);
    }

    @Override
    public Page<Item> searchByKeyword(String keyword, Pageable pageable)
    {
        JPQLQuery<Item> query = from(item)
                                .where(
                                        item.barcode.eq(keyword)
                                        .or(item.name.containsIgnoreCase(keyword))
                                        .or(item.type.in(ItemType.findEnumByName(keyword)))
                                        );

        JPQLQuery<Item> pagination = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Item> results = pagination.fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}

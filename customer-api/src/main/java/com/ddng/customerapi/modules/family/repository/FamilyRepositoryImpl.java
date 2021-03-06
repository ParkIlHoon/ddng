package com.ddng.customerapi.modules.family.repository;

import com.ddng.customerapi.modules.customer.domain.CustomerType;
import com.ddng.customerapi.modules.family.domain.Family;
import com.ddng.customerapi.modules.family.domain.QFamily;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import static com.ddng.customerapi.modules.customer.domain.QCustomer.customer;
import static com.ddng.customerapi.modules.family.domain.QFamily.family;
import static com.ddng.customerapi.modules.tag.domain.QTag.tag;

public class FamilyRepositoryImpl extends QuerydslRepositorySupport implements FamilyCustomRepository
{
    public FamilyRepositoryImpl()
    {
        super(Family.class);
    }

    @Override
    public Page<Family> findByKeyword(String keyword, Pageable pageable)
    {
        QFamily f = new QFamily("f");
        JPQLQuery<Family> query = from(family)
                                    .where(
                                            family.in(
                                                    from(f)
                                                    .where(f.name.containsIgnoreCase(keyword)
                                                            .or(customer.name.containsIgnoreCase(keyword))
                                                            .or(customer.type.in(CustomerType.findEnumByKorNameLike(keyword)))
                                                            .or(customer.telNo.containsIgnoreCase(keyword))
                                                            .or(tag.title.containsIgnoreCase(keyword)))
                                                    .leftJoin(f.customers, customer)
                                                    .leftJoin(customer.tags, tag)
                                                    .distinct()
                                                    )
                                            );

        JPQLQuery<Family> pagination = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Family> queryResults = pagination.fetchResults();
        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }


}

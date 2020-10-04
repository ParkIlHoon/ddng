package com.ddng.customerapi.modules.customer.repository;


import com.ddng.customerapi.modules.customer.domain.CustomerType;
import com.ddng.customerapi.modules.customer.domain.Customer;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import static com.ddng.customerapi.modules.customer.domain.QCustomer.customer;
import static com.ddng.customerapi.modules.tag.domain.QTag.tag;


public class CustomerRepositoryImpl extends QuerydslRepositorySupport implements CustomerCustomRepository
{
    public CustomerRepositoryImpl()
    {
        super(Customer.class);
    }

    @Override
    public Page<Customer> searchByKeyword(String keyword, Pageable pageable)
    {
        JPQLQuery<Customer> query = from(customer)
                                    .where(
                                            customer.name.containsIgnoreCase(keyword)
                                                    .or(customer.type.in(CustomerType.findEnumByKorNameLike(keyword)))
                                                    .or(customer.telNo.containsIgnoreCase(keyword))
                                                    .or(customer.tags.any().title.containsIgnoreCase(keyword))
                                            )
                                    .leftJoin(customer.tags, tag).fetchJoin()
                                    .distinct();

        JPQLQuery<Customer> pagination = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Customer> queryResults = pagination.fetchResults();
        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }
}

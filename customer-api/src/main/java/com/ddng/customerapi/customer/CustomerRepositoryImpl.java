package com.ddng.customerapi.customer;


import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import static com.ddng.customerapi.customer.QCustomer.customer;

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
                                            //TODO 태그 연동
                                            )
                                    .distinct();

        JPQLQuery<Customer> pagination = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Customer> queryResults = pagination.fetchResults();
        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }
}

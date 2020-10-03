package com.ddng.customerapi.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CustomerCustomRepository
{
    Page<Customer> searchByKeyword(String keyword, Pageable pageable);
}

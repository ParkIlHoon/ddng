package com.ddng.customerapi.modules.customer.repository;

import com.ddng.customerapi.modules.customer.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CustomerCustomRepository
{
    Page<Customer> searchByKeyword(String keyword, Pageable pageable);
}

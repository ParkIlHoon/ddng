package com.ddng.customerapi.modules.customer.repository;

import com.ddng.customerapi.modules.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerCustomRepository
{
}

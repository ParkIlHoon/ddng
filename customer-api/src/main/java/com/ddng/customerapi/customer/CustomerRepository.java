package com.ddng.customerapi.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerCustomRepository
{
}

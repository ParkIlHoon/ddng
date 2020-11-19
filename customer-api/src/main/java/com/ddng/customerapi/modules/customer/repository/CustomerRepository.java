package com.ddng.customerapi.modules.customer.repository;

import com.ddng.customerapi.modules.customer.domain.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CustomerRepository extends JpaRepository<Customer, Long>, CustomerCustomRepository
{
    @EntityGraph(attributePaths = {"tags", "family"})
    Optional<Customer> findById(Long id);

    @EntityGraph(attributePaths = {"tags", "family"})
    List<Customer> findByIdIn(List<Long> ids);
}

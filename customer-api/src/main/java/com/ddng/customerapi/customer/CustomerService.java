package com.ddng.customerapi.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService
{
    private final CustomerRepository customerRepository;

    public Page<Customer> findByKeyword(String keyword, Pageable pageable)
    {
        return customerRepository.searchByKeyword(keyword, pageable);
    }

    public Optional<Customer> findById(Long id)
    {
        return customerRepository.findById(id);
    }
}

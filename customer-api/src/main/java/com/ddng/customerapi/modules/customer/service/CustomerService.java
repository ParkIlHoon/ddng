package com.ddng.customerapi.modules.customer.service;

import com.ddng.customerapi.modules.customer.domain.Customer;
import com.ddng.customerapi.modules.customer.dto.CustomerDto;
import com.ddng.customerapi.modules.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    public Page<Customer> findByKeyword(String keyword, Pageable pageable)
    {
        return customerRepository.searchByKeyword(keyword, pageable);
    }

    public Optional<Customer> findById(Long id)
    {
        return customerRepository.findById(id);
    }

    public Customer updateCustomerInfo(Long id, CustomerDto.Put customerDto)
    {
        Customer findById = this.findById(id).orElseThrow();

        modelMapper.map(customerDto, findById);

        Customer save = customerRepository.save(findById);

        return save;
    }

    public Customer createCustomer(CustomerDto.Post dto)
    {
        Customer map = modelMapper.map(dto, Customer.class);
        Customer save = customerRepository.save(map);
        return save;
    }

    public void deleteCustomer(Long id)
    {
        Customer customer = this.findById(id).orElseThrow();
        customerRepository.delete(customer);
    }
}

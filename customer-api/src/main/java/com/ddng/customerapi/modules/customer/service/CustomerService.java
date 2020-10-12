package com.ddng.customerapi.modules.customer.service;

import com.ddng.customerapi.modules.customer.domain.Customer;
import com.ddng.customerapi.modules.customer.dto.CustomerDto;
import com.ddng.customerapi.modules.customer.repository.CustomerRepository;
import com.ddng.customerapi.modules.tag.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <h1>고객 서비스 클래스</h1>
 *
 * @version 1.0
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService
{
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    /**
     * 키워드로 고객 목록을 검색한다.
     * @param keyword 검색할 고객의 이름/품종/전화번호 키워드
     * @param pageable 페이징 정보
     * @return 키워드에 해당하는 고객 목록
     */
    public Page<CustomerDto.Response> findByKeyword(String keyword, Pageable pageable)
    {
        Page<Customer> customers = customerRepository.searchByKeyword(keyword, pageable);

        List<CustomerDto.Response> collect = customers.getContent().stream()
                                                                    .map(customer -> new CustomerDto.Response(customer))
                                                                    .collect(Collectors.toList());
        return new PageImpl<>(collect, pageable, customers.getTotalElements());
    }

    /**
     * 고객을 조회한다.
     * @param id 조회할 고객의 아이디
     * @return 아이디에 해당하는 고객 정보
     */
    public Optional<Customer> findById(Long id)
    {
        return customerRepository.findById(id);
    }

    /**
     * 고객 정보를 수정한다.
     * @param id 수정할 고객의 아이디
     * @param customerDto 수정할 고객 정보
     * @return 수정된 고객 정보
     */
    public Customer updateCustomerInfo(Long id, CustomerDto.Put customerDto)
    {
        Customer findById = this.findById(id).orElseThrow();
        modelMapper.map(customerDto, findById);
        Customer save = customerRepository.save(findById);
        return save;
    }

    /**
     * 고객을 생성한다.
     * @param dto 생성할 고객 정보
     * @return 생성된 고객 정보
     */
    public Customer createCustomer(CustomerDto.Post dto)
    {
        Customer map = modelMapper.map(dto, Customer.class);
        Customer save = customerRepository.save(map);
        return save;
    }

    /**
     * 고객을 제거한다.
     * @param id 제거할 고객 아이디
     */
    public void deleteCustomer(Long id)
    {
        Customer customer = this.findById(id).orElseThrow();
        customerRepository.delete(customer);
    }

    /**
     * 고객 정보에 태그를 추가한다.
     * @param customer 태그를 추가할 고객
     * @param tag 추가할 태그
     */
    public void addTag(Customer customer, Tag tag)
    {
        customer.getTags().add(tag);
    }

    /**
     * 고객 정보의 태그를 제거한다.
     * @param customer 태그를 제거할 고객
     * @param tag 제거할 태그
     */
    public void removeTag(Customer customer, Tag tag)
    {
        customer.getTags().remove(tag);
    }
}

package com.ddng.customerapi.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController
{
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity getCustomerList(String keyword,
                                          @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable)
    {
        Page<Customer> findByKeyword = customerService.findByKeyword(keyword, pageable);
        return ResponseEntity.ok(findByKeyword.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity getCustomer(@PathVariable("id") Long id)
    {
        Optional<Customer> customer = customerService.findById(id);

        if (customer.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(customer.get());
    }
}

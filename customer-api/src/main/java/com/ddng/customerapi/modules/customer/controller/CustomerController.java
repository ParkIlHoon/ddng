package com.ddng.customerapi.modules.customer.controller;

import com.ddng.customerapi.modules.customer.dto.CustomerDto;
import com.ddng.customerapi.modules.customer.service.CustomerService;
import com.ddng.customerapi.modules.customer.domain.Customer;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * <h1>고객 관련 요청 처리 API 컨트롤러</h1>
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController
{
    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    /**
     * 고객 목록을 검색한다.
     * @param keyword 검색 키워드
     * @param pageable 페이지 처리
     * @return 검색 키워드와 페이지에 해당하는 고객 목록
     */
    @GetMapping
    public ResponseEntity getCustomerList(String keyword,
                                          @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable)
    {
        Page<Customer> findByKeyword = customerService.findByKeyword(keyword, pageable);
        return ResponseEntity.ok(findByKeyword.getContent());
    }

    /**
     * 
     * @param dto
     * @param errors
     * @return
     */
    @PostMapping
    public ResponseEntity postCustomer(@RequestBody @Valid CustomerDto.Post dto, Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.badRequest().build();
        }

        Customer created = customerService.createCustomer(dto);
        WebMvcLinkBuilder builder = linkTo(CustomerController.class).slash(created.getId());
        return ResponseEntity.created(builder.toUri()).build();
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

    @PutMapping("/{id}")
    public ResponseEntity putCustomer(@PathVariable("id") Long id,
                                      @RequestBody @Valid CustomerDto.Put customerDto,
                                      Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.badRequest().build();
        }

        Customer updated = customerService.updateCustomerInfo(id, customerDto);
        CustomerDto.Response response = modelMapper.map(updated, CustomerDto.Response.class);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCustomer(@PathVariable("id") Long id)
    {
        Optional<Customer> optional = customerService.findById(id);

        if(optional.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }

        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }
}

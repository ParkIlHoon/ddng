package com.ddng.customerapi.modules.customer.controller;

import com.ddng.customerapi.modules.customer.domain.CustomerType;
import com.ddng.customerapi.modules.customer.dto.CustomerDto;
import com.ddng.customerapi.modules.customer.service.CustomerService;
import com.ddng.customerapi.modules.customer.domain.Customer;
import com.ddng.customerapi.modules.family.domain.Family;
import com.ddng.customerapi.modules.family.service.FamilyService;
import com.ddng.customerapi.modules.tag.domain.Tag;
import com.ddng.customerapi.modules.tag.dto.TagDto;
import com.ddng.customerapi.modules.tag.repository.TagRepository;
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
import java.util.*;
import java.util.stream.Collectors;

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
    private final FamilyService familyService;
    private final TagRepository tagRepository;
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
        Page<CustomerDto.Response> findByKeyword = customerService.findByKeyword(keyword, pageable);
        return ResponseEntity.ok(findByKeyword);
    }

    /**
     * 고객 리스트를 조회한다
     * @param keyword 조회할 고객 아이디 리스트
     * @return 고객 아이디 리스트에 해당하는 고객 목록
     */
    @GetMapping("/in")
    public ResponseEntity getCustomerListAsIn(String keyword)
    {
        String[] split = keyword.split(",");
        List<Long> collect = Arrays.stream(split).map(s -> Long.valueOf(s)).collect(Collectors.toList());

        List<CustomerDto.Response> byIds = customerService.findByIds(collect);
        return ResponseEntity.ok(byIds);
    }


    /**
     * 고객을 신규 생성한다.
     * @param dto 신규 생성할 고객 정보
     * @param errors
     * @return 신규 생성된 고객 정보를 조회하는 URL 정보
     */
    @PostMapping
    public ResponseEntity postCustomer(@RequestBody @Valid CustomerDto.Post dto, Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.badRequest().build();
        }
        Family family = null;
        if (dto.getFamilyId() != null)
        {
            Optional<Family> optionalFamily = familyService.findById(dto.getFamilyId());
            if (optionalFamily.isEmpty())
            {
                return ResponseEntity.badRequest().build();
            }
            family = optionalFamily.get();
        }

        Customer created = customerService.createCustomer(dto);

        if (family != null)
        {
            familyService.addMember(family, created);
        }
        else
        {
            familyService.createFamily(created);
        }

        WebMvcLinkBuilder builder = linkTo(CustomerController.class).slash(created.getId());
        return ResponseEntity.created(builder.toUri()).build();
    }

    /**
     * 특정 고객을 조회한다.
     * @param id 조회할 고객의 아이디
     * @return 아이디에 해당하는 고객
     */
    @GetMapping("/{id}")
    public ResponseEntity getCustomer(@PathVariable("id") Long id)
    {
        Optional<Customer> customer = customerService.findById(id);

        if (customer.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }

        CustomerDto.Response response = new CustomerDto.Response(customer.get());
        response.createFamilyString();
        return ResponseEntity.ok(response);
    }

    /**
     * 고객 정보를 수정한다.
     * @param id 수정할 고객의 아이디
     * @param customerDto 수정할 고객 정보
     * @param errors
     * @return 수정된 고객 정보
     */
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
        CustomerDto.Response response = new CustomerDto.Response(updated);
        response.createFamilyString();
        return ResponseEntity.ok(response);
    }

    /**
     * 특정 고객을 제거한다.
     * @param id 제거할 고객의 아이디
     * @return 제거 성공 여부
     */
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

    /**
     * 특정 고객에 태그를 추가한다.
     * @param customerId 태그를 추가할 고객의 아이디
     * @param dto 추가할 태그 정보
     * @return
     */
    @PostMapping("/{id}/tag")
    public ResponseEntity postCustomerTag (@PathVariable("id") Long customerId,
                                           @RequestBody TagDto dto)
    {
        Optional<Customer> optionalCustomer = customerService.findById(customerId);
        if(optionalCustomer.isEmpty())
        {
            return ResponseEntity.badRequest().build();
        }

        Optional<Tag> optionalTag = tagRepository.findByTitle(dto.getTitle());
        Tag tag;
        if(optionalTag.isEmpty())
        {
            tag = tagRepository.save(Tag.builder().title(dto.getTitle()).build());
        }
        else
        {
            tag = optionalTag.get();
        }

        customerService.addTag(optionalCustomer.get(), tag);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 고객의 태그를 제거한다.
     * @param customerId 태그를 제거할 고객의 아이디
     * @param dto 제거할 태그 정보
     * @return
     */
    @DeleteMapping("{id}/tag")
    public ResponseEntity deleteCustomerTag (@PathVariable("id") Long customerId,
                                             @RequestBody TagDto dto)

    {
        Optional<Customer> optionalCustomer = customerService.findById(customerId);
        if(optionalCustomer.isEmpty())
        {
            return ResponseEntity.badRequest().build();
        }

        Optional<Tag> optionalTag = tagRepository.findByTitle(dto.getTitle());
        if(optionalTag.isEmpty())
        {
            return ResponseEntity.badRequest().build();
        }

        customerService.removeTag(optionalCustomer.get(), optionalTag.get());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/type")
    public ResponseEntity getCustomerType ()
    {
        List returnList = new ArrayList();
        CustomerType[] types = CustomerType.values();
        for (CustomerType type : types)
        {
            HashMap row = new HashMap();
            row.put("id", type.name());
            row.put("text", type.getKorName());

            returnList.add(row);
        }

        return ResponseEntity.ok(returnList);
    }
}

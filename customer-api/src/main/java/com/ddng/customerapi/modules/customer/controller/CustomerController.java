package com.ddng.customerapi.modules.customer.controller;

import com.ddng.customerapi.modules.customer.domain.CustomerType;
import com.ddng.customerapi.modules.customer.dto.CustomerDto;
import com.ddng.customerapi.modules.customer.dto.CustomerTypeDto;
import com.ddng.customerapi.modules.customer.service.CustomerService;
import com.ddng.customerapi.modules.customer.domain.Customer;
import com.ddng.customerapi.modules.family.domain.Family;
import com.ddng.customerapi.modules.family.service.FamilyService;
import com.ddng.customerapi.modules.tag.domain.Tag;
import com.ddng.customerapi.modules.tag.dto.TagDto;
import com.ddng.customerapi.modules.tag.repository.TagRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(value = "고객 컨트롤러")
@RequestMapping("/customers")
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
    @ApiOperation(value = "고객 목록 검색", notes = "검색 키워드에 해당하는 고객 목록을 조회합니다.")
    public ResponseEntity getCustomerList(@ApiParam(value = "검색할 키워드", required = true) String keyword,
                                          @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable)
    {
        Page<CustomerDto.Response> findByKeyword = customerService.findByKeyword(keyword, pageable);
        return ResponseEntity.ok(findByKeyword);
    }

    /**
     * 고객 리스트를 조회한다
     * @param customerIds 조회할 고객 아이디 배열
     * @return 고객 아이디 리스트에 해당하는 고객 목록
     */
    @GetMapping("/in")
    @ApiOperation(value = "고객 목록 검색", notes = "아이디 배열에 해당하는 고객 목록을 조회합니다.")
    public ResponseEntity getCustomerListAsIn(@ApiParam(value = "조회할 고객 아이디 배열", required = true) Long[] customerIds)
    {
        List<Long> collect = Arrays.stream(customerIds).collect(Collectors.toList());
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
    @ApiOperation(value = "신규 고객 생성", notes = "요청 정보로 신규 고객을 생성합니다.")
    public ResponseEntity postCustomer(@ApiParam(value = "신규 고객 생성 정보") @RequestBody @Valid CustomerDto.Post dto, Errors errors)
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
    @ApiOperation(value = "고객 조회", notes = "아이디에 해당하는 고객 정보를 조회합니다.")
    public ResponseEntity getCustomer(@ApiParam(value = "조회할 고객의 아이디", required = true) @PathVariable("id") Long id)
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
    @ApiOperation(value = "고객 수정", notes = "아이디에 해당하는 고객 정보를 수정합니다.")
    public ResponseEntity putCustomer(@ApiParam(value = "수정할 고객의 아이디", required = true) @PathVariable("id") Long id,
                                      @ApiParam(value = "수정할 고객 정보", required = true) @RequestBody @Valid CustomerDto.Put customerDto,
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
    @ApiOperation(value = "고객 삭제", notes = "아이디에 해당하는 고객을 제거합니다.")
    public ResponseEntity deleteCustomer(@ApiParam(value = "제거할 고객의 아이디", required = true) @PathVariable("id") Long id)
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
    @ApiOperation(value = "고객 태그 추가", notes = "아이디에 해당하는 고객에 태그를 추가합니다.")
    public ResponseEntity postCustomerTag (@ApiParam(value = "고객의 아이디", required = true) @PathVariable("id") Long customerId,
                                           @ApiParam(value = "추가할 태그 정보", required = true) @RequestBody TagDto dto)
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
    @ApiOperation(value = "고객 태그 제거", notes = "아이디에 해당하는 고객의 특정 태그를 제거합니다.")
    public ResponseEntity deleteCustomerTag (@ApiParam(value = "고객의 아이디", required = true) @PathVariable("id") Long customerId,
                                             @ApiParam(value = "제거할 태그 정보", required = true) @RequestBody TagDto dto)

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

    /**
     * 고객 종류 목록을 반환한다.
     * @return
     */
    @GetMapping("/types")
    @ApiOperation(value = "고객 종류 조회", notes = "고객의 종류 목록을 조회합니다.")
    public ResponseEntity getCustomerType ()
    {
        List<CustomerTypeDto> collect = new ArrayList<>();
        CustomerType[] values = CustomerType.values();
        for(CustomerType type : values)
        {
            collect.add(new CustomerTypeDto(type));
        }
        return ResponseEntity.ok(collect);
    }
}

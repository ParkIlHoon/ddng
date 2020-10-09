package com.ddng.customerapi.modules.family.controller;

import com.ddng.customerapi.modules.customer.domain.Customer;
import com.ddng.customerapi.modules.customer.service.CustomerService;
import com.ddng.customerapi.modules.family.domain.Family;
import com.ddng.customerapi.modules.family.dto.FamilyDto;
import com.ddng.customerapi.modules.family.service.FamilyService;
import lombok.RequiredArgsConstructor;
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
 * <h1>가족 관련 요청 처리 API 컨트롤러</h1>
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/family")
@RequiredArgsConstructor
public class FamilyController
{
    private final FamilyService familyService;
    private final CustomerService customerService;

    /**
     * 가족 목록을 검색한다.
     * @param keyword 검색할 키워드
     * @param pageable 페이지 처리
     * @return 키워드에 해당하는 가족 목록
     */
    @GetMapping
    public ResponseEntity getFamilyList (String keyword,
                                         @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable)
    {
        Page<Family> familyList = familyService.findByKeyword(keyword, pageable);
        return ResponseEntity.ok(familyList.getContent());
    }

    /**
     * 특정 가족을 조회한다.
     * @param id 조회할 가족 아이디
     * @return 아이디에 해당하는 가족
     */
    @GetMapping("/{id}")
    public ResponseEntity getFamily (@PathVariable("id") Long id)
    {
        Optional<Family> optionalFamily = familyService.findById(id);

        if (optionalFamily.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(optionalFamily.get());
    }

    /**
     * 가족을 생성한다.
     * @param dto 가족 기본 멤버 정보
     * @param errors
     * @return 생성된 가족 URI
     */
    @PostMapping
    public ResponseEntity postFamily (@RequestBody @Valid FamilyDto.Post dto,
                                      Errors errors)
    {
        Optional<Customer> optionalCustomer = customerService.findById(dto.getCustomerId());

        if (optionalCustomer.isEmpty())
        {
            return ResponseEntity.badRequest().build();
        }

        Family family = familyService.createFamily(optionalCustomer.get());

        WebMvcLinkBuilder builder = linkTo(FamilyController.class).slash(family.getId());
        return ResponseEntity.created(builder.toUri()).build();
    }

    /**
     * 가족을 제거한다.
     * @param id 제거할 가족 아이디
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteFamily (@PathVariable("id") Long id)
    {
        Optional<Family> optionalFamily = familyService.findById(id);

        if (optionalFamily.isEmpty())
        {
            return ResponseEntity.badRequest().build();
        }

        familyService.deleteFamily(optionalFamily.get());
        return ResponseEntity.ok().build();
    }

    /**
     * 가족에 멤버를 추가한다.
     * @param id 추가할 가족 아이디
     * @param dto 추가할 고객 정보
     * @param errors
     * @return
     */
    @PostMapping("/{id}/member")
    public ResponseEntity addMember (@PathVariable("id") Long id,
                                     @RequestBody @Valid FamilyDto.Post dto,
                                     Errors errors)
    {
        Optional<Family> optionalFamily = familyService.findById(id);

        if (optionalFamily.isEmpty())
        {
            return ResponseEntity.badRequest().build();
        }

        Optional<Customer> optionalCustomer = customerService.findById(dto.getCustomerId());

        if (optionalCustomer.isEmpty())
        {
            return ResponseEntity.badRequest().build();
        }

        familyService.addMember(optionalFamily.get(), optionalCustomer.get());

        return ResponseEntity.ok().build();
    }

    /**
     * 가족의 멤버를 제거한다.
     * @param id 제거할 가족 아이디
     * @param dto 제거할 고객 정보
     * @return
     */
    @DeleteMapping("/{id}/member")
    public ResponseEntity removeMember (@PathVariable("id") Long id,
                                        @RequestBody @Valid FamilyDto.Delete dto)
    {
        Optional<Family> optionalFamily = familyService.findById(id);

        if (optionalFamily.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Customer> optionalCustomer = customerService.findById(dto.getCustomerId());

        if (optionalCustomer.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        familyService.removeMember(optionalFamily.get(), optionalCustomer.get());
        return ResponseEntity.ok().build();
    }

}

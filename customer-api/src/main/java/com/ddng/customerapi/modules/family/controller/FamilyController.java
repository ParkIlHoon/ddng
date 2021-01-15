package com.ddng.customerapi.modules.family.controller;

import com.ddng.customerapi.modules.customer.domain.Customer;
import com.ddng.customerapi.modules.customer.service.CustomerService;
import com.ddng.customerapi.modules.family.domain.Family;
import com.ddng.customerapi.modules.family.dto.FamilyDto;
import com.ddng.customerapi.modules.family.service.FamilyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(value = "가족 컨트롤러")
@RequestMapping("/families")
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
    @ApiOperation(value = "가족 목록 검색", notes = "검색 키워드에 해당하는 가족 목록을 조회합니다.")
    public ResponseEntity getFamilyList (@ApiParam(value = "검색할 키워드", required = true, example = "치우네") String keyword,
                                         @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable)
    {
        Page<FamilyDto.Response> familyList = familyService.findByKeyword(keyword, pageable);
        return ResponseEntity.ok(familyList);
    }

    /**
     * 특정 가족을 조회한다.
     * @param id 조회할 가족 아이디
     * @return 아이디에 해당하는 가족
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "가족 조회", notes = "특정 아이디에 해당하는 가족을 조회합니다.")
    public ResponseEntity getFamily (@ApiParam(value = "조회할 가족 아이디", required = true, example = "1") @PathVariable("id") Long id)
    {
        Optional<Family> optionalFamily = familyService.findById(id);

        if (optionalFamily.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }

        FamilyDto.ResponseWithCustomerTag dto = new FamilyDto.ResponseWithCustomerTag(optionalFamily.get());

        return ResponseEntity.ok(dto);
    }

    /**
     * 가족을 생성한다.
     * @param dto 가족 기본 멤버 정보
     * @param errors
     * @return 생성된 가족 URI
     */
    @PostMapping
    @ApiOperation(value = "가족 생성", notes = "새로운 가족을 생성합니다.")
    public ResponseEntity postFamily (@ApiParam(value = "생성할 가족 정보", required = true) @RequestBody @Valid FamilyDto.Post dto,
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
     * 가족을 수정한다.
     * @param id 수정할 가족 아이디
     * @param dto 수정할 정보
     * @param errors
     * @return
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "가족 정보 수정", notes = "가족의 정보를 수정합니다.")
    public ResponseEntity updateFamily(@ApiParam(value = "수정할 가족 아이디", required = true, example = "1") @PathVariable("id") Long id,
                                       @ApiParam(value = "수정할 정보", required = true) @RequestBody @Valid FamilyDto.Put dto,
                                       Errors errors)
    {
        Optional<Family> optionalFamily = familyService.findById(id);

        if (optionalFamily.isEmpty())
        {
            return ResponseEntity.badRequest().build();
        }

        familyService.updateFamily(optionalFamily.get(), dto);
        return ResponseEntity.ok().build();
    }

    /**
     * 가족을 제거한다.
     * @param id 제거할 가족 아이디
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "가족 삭제", notes = "가족을 삭제합니다.")
    public ResponseEntity updateFamily(@ApiParam(value = "삭제할 가족 아이디", required = true, example = "1") @PathVariable("id") Long id)
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
    @ApiOperation(value = "가족 구성원 추가", notes = "가족에 구성원을 추가합니다.")
    public ResponseEntity addMember (@ApiParam(value = "가족 아이디", required = true, example = "1") @PathVariable("id") Long id,
                                     @ApiParam(value = "추가할 구성원 정보", required = true) @RequestBody @Valid FamilyDto.Post dto,
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
    @ApiOperation(value = "가족 구성원 제거", notes = "가족의 구성원을 제거합니다.")
    public ResponseEntity removeMember (@ApiParam(value = "가족 아이디", required = true, example = "1") @PathVariable("id") Long id,
                                        @ApiParam(value = "삭제할 구성원 정보", required = true) @RequestBody @Valid FamilyDto.Delete dto)
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

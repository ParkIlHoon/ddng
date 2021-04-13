package com.ddng.adminuibootstrap.modules.common.clients;

import com.ddng.adminuibootstrap.modules.common.dto.FeignPageImpl;
import com.ddng.adminuibootstrap.modules.common.dto.customer.CustomerDto;
import com.ddng.adminuibootstrap.modules.common.dto.customer.CustomerTagDto;
import com.ddng.adminuibootstrap.modules.common.dto.customer.CustomerTypeDto;
import com.ddng.adminuibootstrap.modules.common.dto.customer.FamilyDto;
import com.ddng.adminuibootstrap.modules.customer.form.EditForm;
import com.ddng.adminuibootstrap.modules.customer.form.FamilySettingForm;
import com.ddng.adminuibootstrap.modules.customer.form.RegisterForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <h1>Customer api Feign Client</h1>
 *
 * Spring Cloud Feign 기능을 사용해 RestTemplate 코드를 작성하지 않고 요청 기능을 구현한다.
 */
@FeignClient(name = "ddng-customer-api")
public interface CustomerClient
{
    String CUSTOMER_API = "/customers";
    String FAMILY_API   = "/families";

    /**
     * 고객 종류를 조회하는 API를 호출한다.
     * @return 고객 종류 리스트
     */
    @GetMapping(CUSTOMER_API + "/types")
    List<CustomerTypeDto> getCustomerTypes();

    /**
     * 가족 목록을 조회하는 API를 호출한다.
     * @param keyword 조회할 키워드
     * @return 가족 목록 리스트
     */
    @GetMapping(FAMILY_API)
    FeignPageImpl<FamilyDto> searchFamilies(@RequestParam("keyword") String keyword);

    /**
     * 가족 목록을 페이징 처리해 조회하는 API를 호출한다.
     * @param keyword 조회할 키워드
     * @param page 조회할 페이지
     * @param size 조회할 건수
     * @return 가족 목록 리스트
     */
    @GetMapping(FAMILY_API)
    FeignPageImpl<FamilyDto> searchFamiliesWithPage(@RequestParam("keyword") String keyword,
                                                    @RequestParam("page") int page,
                                                    @RequestParam("size") int size);

    /**
     * 가족을 조회하는 API를 호출한다.
     * @param familyId 조회할 가족의 아이디
     * @return 아이디에 해당하는 가족
     */
    @GetMapping(FAMILY_API + "/{familyId}")
    FamilyDto getFamily(@PathVariable("familyId") Long familyId);

    /**
     * 고객을 생성하는 API를 호출한다.
     * @param registerForm 생성할 고객 정보
     */
    @PostMapping(value = CUSTOMER_API)
    void createCustomer(@RequestBody RegisterForm registerForm);

    /**
     * 가족 설정을 수정하는 API를 호출한다.
     * @param familyId 설정을 수정할 가족 아이디
     * @param familySettingForm 수정할 설정 내용
     */
    @PutMapping(FAMILY_API + "/{familyId}")
    void updateFamilySetting(@PathVariable("familyId") Long familyId, FamilySettingForm familySettingForm);

    /**
     * 고객 목록을 페이징 처리해 조회하는 API를 호출한다.
     * @param keyword 조회할 키워드
     * @param page 조회할 페이지
     * @param size 조회할 건수
     * @return 고객 목록 리스트
     */
    @GetMapping(CUSTOMER_API)
    FeignPageImpl<CustomerDto> searchCustomersWithPage(@RequestParam("keyword") String keyword,
                                                       @RequestParam("page") int page,
                                                       @RequestParam("size") int size);

    /**
     * 아이디 목록에 해당하는 고객 목록을 조회하는 API를 호출한다.
     * @param customerIds 조회할 고객 아이디 리스
     * @return 아이디에 해당하는 고객 목록
     */
    @GetMapping(CUSTOMER_API + "/in")
    List<CustomerDto> getCustomers(@RequestParam("customerIds") List<Long> customerIds);

    /**
     * 고객을 조회하는 API를 호출한다.
     * @param customerId 조회할 고객의 아이디
     * @return 아이디에 해당하는 고객
     */
    @GetMapping(CUSTOMER_API + "/{customerId}")
    CustomerDto getCustomer(@PathVariable("customerId") Long customerId);

    /**
     * 고객 정보를 수정하는 API를 호출한다.
     * @param customerId 수정할 고객의 아이디
     * @param editForm 수정할 정보
     */
    @PutMapping(CUSTOMER_API + "/{customerId}")
    void updateCustomer(@PathVariable("customerId") Long customerId, EditForm editForm);

    /**
     * 전체 고객 태그 목록을 조회하는 API를 호출한다.
     * @return 고객 태그 목록
     */
    @GetMapping("/tags")
    List<CustomerTagDto> getCustomerTags();

    /**
     * 고객에 태그를 추가하는 API를 호출한다.
     * @param customerId 태그를 추가할 고객의 아이디
     * @param title 추가할 태그 타이틀
     */
    @PostMapping(CUSTOMER_API + "/{customerId}/tags")
    void addCustomerTag(@PathVariable("customerId") Long customerId, @RequestBody String title);

    /**
     * 고객에 태그를 제거하는 API를 호출한다.
     * @param customerId 태그를 제거할 고객의 아이디
     * @param title 제거할 태그 타이틀
     */
    @DeleteMapping(CUSTOMER_API + "/{customerId}/tags")
    void removeCustomerTag(@PathVariable("customerId") Long customerId, @RequestBody String title);
}

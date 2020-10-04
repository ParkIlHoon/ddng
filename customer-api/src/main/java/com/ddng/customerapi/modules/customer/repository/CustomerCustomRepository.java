package com.ddng.customerapi.modules.customer.repository;

import com.ddng.customerapi.modules.customer.domain.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h1>고객 커스텀 리포지토리 클래스</h1>
 *
 * @version 1.0
 */
@Transactional(readOnly = true)
public interface CustomerCustomRepository
{
    /**
     * 키워드로 고객을 검색한다.
     * @param keyword 검색할 키워드(고객명,품종명,전화번호 대상)
     * @param pageable 페이징 정보
     * @return 키워드에 해당하는 고객 목록
     */
    Page<Customer> searchByKeyword(String keyword, Pageable pageable);
}

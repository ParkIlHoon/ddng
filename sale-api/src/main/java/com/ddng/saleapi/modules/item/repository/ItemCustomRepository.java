package com.ddng.saleapi.modules.item.repository;

import com.ddng.saleapi.modules.item.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <h1>상품 커스텀 리포지토리 인터페이스</h1>
 *
 * @version 1.0
 */
@Transactional(readOnly = true)
public interface ItemCustomRepository
{
    /**
     * 키워드로 상품을 검색한다.
     * @param keyword 검색할 키워드(상품명, 상품 종류, 바코드)
     * @param pageable 페이징 정보
     * @return
     */
    Page<Item> searchByKeyword(String keyword, Pageable pageable);

    /**
     * 키워드로 미용 상품을 검색한다.
     * @param keyword 검색할 키워드(상품명, 바코드)
     * @param pageable
     * @return
     */
    Page<Item> searchBeautyItemsByKeyword(String keyword, Pageable pageable);
}

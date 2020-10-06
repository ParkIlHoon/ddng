package com.ddng.customerapi.modules.family.controller;

import com.ddng.customerapi.modules.family.domain.Family;
import com.ddng.customerapi.modules.family.service.FamilyService;
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
}

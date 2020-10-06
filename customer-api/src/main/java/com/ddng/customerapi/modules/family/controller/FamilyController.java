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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/family")
@RequiredArgsConstructor
public class FamilyController
{
    private final FamilyService familyService;

    @GetMapping
    public ResponseEntity getFamilyList (String keyword,
                                         @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable)
    {
        Page<Family> familyList = familyService.findByKeyword(keyword, pageable);

        return ResponseEntity.ok(familyList.getContent());
    }
}

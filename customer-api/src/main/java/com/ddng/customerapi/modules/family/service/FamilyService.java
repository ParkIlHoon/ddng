package com.ddng.customerapi.modules.family.service;

import com.ddng.customerapi.modules.family.domain.Family;
import com.ddng.customerapi.modules.family.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FamilyService
{
    private final FamilyRepository familyRepository;

    public Page<Family> findByKeyword(String keyword, Pageable pageable)
    {
        return familyRepository.findByKeyword(keyword, pageable);
    }
}

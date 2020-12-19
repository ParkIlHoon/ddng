package com.ddng.customerapi.modules.family.repository;

import com.ddng.customerapi.modules.family.domain.Family;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface FamilyCustomRepository
{
    Page<Family> findByKeyword(String keyword, Pageable pageable);
}

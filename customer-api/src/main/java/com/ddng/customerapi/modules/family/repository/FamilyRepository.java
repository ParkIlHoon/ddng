package com.ddng.customerapi.modules.family.repository;

import com.ddng.customerapi.modules.family.domain.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface FamilyRepository extends JpaRepository<Family, Long>, FamilyCustomRepository
{

}

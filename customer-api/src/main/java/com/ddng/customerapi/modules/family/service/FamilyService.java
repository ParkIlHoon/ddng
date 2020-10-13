package com.ddng.customerapi.modules.family.service;

import com.ddng.customerapi.modules.customer.domain.Customer;
import com.ddng.customerapi.modules.family.domain.Family;
import com.ddng.customerapi.modules.family.dto.FamilyDto;
import com.ddng.customerapi.modules.family.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FamilyService
{
    private final FamilyRepository familyRepository;

    public Page<FamilyDto.Response> findByKeyword(String keyword, Pageable pageable)
    {
        Page<Family> byKeyword = familyRepository.findByKeyword(keyword, pageable);

        List<FamilyDto.Response> collect = byKeyword.getContent().stream().map(family -> new FamilyDto.Response(family))
                                                                .collect(Collectors.toList());
        return new PageImpl<>(collect, pageable, byKeyword.getTotalElements());
    }

    public Optional<Family> findById(Long id)
    {
        return familyRepository.findById(id);
    }

    public void addMember(Family family, Customer customer)
    {
        family.addMember(customer);
    }

    public Family createFamily(Customer customer)
    {
        Family family = new Family(customer);
        return familyRepository.save(family);
    }

    public void deleteFamily(Family family)
    {
        family.removeAllMember();
        familyRepository.delete(family);
    }

    public void removeMember(Family family, Customer customer)
    {
        family.removeMember(customer);
        family.changeName();
    }
}

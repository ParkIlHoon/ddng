package com.ddng.customerapi.modules.family.service;

import com.ddng.customerapi.modules.customer.domain.Customer;
import com.ddng.customerapi.modules.family.domain.Family;
import com.ddng.customerapi.modules.family.repository.FamilyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        List<Customer> customers = family.getCustomers();
        for (Customer customer : customers)
        {
            family.removeMember(customer);
        }
        familyRepository.delete(family);
    }

    public void removeMember(Family family, Customer customer)
    {
        family.removeMember(customer);
        family.changeName();
    }
}

package com.ddng.saleapi.modules.item.repository;

import com.ddng.saleapi.modules.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface ItemRepository extends JpaRepository<Item, Long>, ItemCustomRepository
{
}

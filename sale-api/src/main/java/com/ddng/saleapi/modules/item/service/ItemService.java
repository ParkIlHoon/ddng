package com.ddng.saleapi.modules.item.service;

import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.item.dto.ItemDto;
import com.ddng.saleapi.modules.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>상품 서비스 클래스</h1>
 *
 * @version 1.0
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ItemService
{
    private final ItemRepository itemRepository;

    public Page<ItemDto.Response> searchByKeyword(String keyword, Pageable pageable)
    {
        Page<Item> items = itemRepository.searchByKeyword(keyword, pageable);

        List<ItemDto.Response> collect = items.getContent().stream()
                                                            .map(item -> new ItemDto.Response(item))
                                                            .collect(Collectors.toList());

        return new PageImpl<>(collect, pageable, items.getTotalElements());
    }
}

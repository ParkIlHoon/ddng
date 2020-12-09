package com.ddng.saleapi.modules.item.service;

import com.ddng.saleapi.infra.util.FileUtils;
import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.item.domain.ItemType;
import com.ddng.saleapi.modules.item.dto.ItemDto;
import com.ddng.saleapi.modules.item.repository.ItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.aspectj.util.FileUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final ModelMapper modelMapper;
    private final FileUtils fileUtils;

    public Page<ItemDto.Response> searchByKeyword(String keyword, Pageable pageable)
    {
        Page<Item> items = itemRepository.searchByKeyword(keyword, pageable);

        List<ItemDto.Response> collect = items.getContent().stream()
                                                            .map(item -> new ItemDto.Response(item))
                                                            .collect(Collectors.toList());

        return new PageImpl<>(collect, pageable, items.getTotalElements());
    }

    public Optional<Item> findById(Long id)
    {
        return itemRepository.findById(id);
    }

    public Page<ItemDto.Response> searchBeautyItemsByKeyword(String keyword, Pageable pageable)
    {
        Page<Item> items = itemRepository.searchBeautyItemsByKeyword(keyword, pageable);
        List<ItemDto.Response> collect = items.getContent().stream()
                .map(item -> new ItemDto.Response(item))
                .collect(Collectors.toList());

        return new PageImpl<>(collect, pageable, items.getTotalElements());
    }

    public List<ItemDto.Response> findHotelItems()
    {
        List<Item> items = itemRepository.findByType(ItemType.HOTEL);
        List<ItemDto.Response> collect = items.stream()
                                                .map(item -> new ItemDto.Response(item))
                                                .collect(Collectors.toList());

        return collect;
    }

    public List<ItemDto.Response> findKindergartenItems()
    {
        List<Item> items = itemRepository.findByType(ItemType.KINDERGARTEN);
        List<ItemDto.Response> collect = items.stream()
                                                .map(item -> new ItemDto.Response(item))
                                                .collect(Collectors.toList());

        return collect;
    }

    public Item createItem(ItemDto.Post dto)
    {
        Item map = modelMapper.map(dto, Item.class);

        if (StringUtils.hasText(map.getItemImg()))
        {
            String uploadPath = "sale/item/" + map.getId() + ".png";
            fileUtils.saveBase64AsFile(uploadPath, map.getItemImg());
            map.setItemImg(uploadPath);
        }

        return itemRepository.save(map);
    }

    public Item updateItem(Item item, ItemDto.Put dto)
    {
        modelMapper.map(dto, item);

        if (StringUtils.hasText(dto.getItemImg()))
        {
            String uploadPath = "sale/item/" + item.getId() + ".png";
            fileUtils.saveBase64AsFile(uploadPath, dto.getItemImg());
            item.setItemImg(uploadPath);
        }

        return itemRepository.save(item);
    }

    public void deleteItem(Item item)
    {
        itemRepository.delete(item);
    }

    public List<String> getAvailableBarcodes(int count)
    {
        List<String> returnList = new ArrayList<>();

        Item item = itemRepository.findTopByBarcodeStartsWithOrderByBarcodeDesc("0000");
        Long aLong = Long.valueOf(item.getBarcode());

        for (int idx = 0; idx < count; idx++)
        {
            aLong++;
            returnList.add(String.format("%" + 13 + "s", aLong).replace(" ", "0"));
        }

        return returnList;
    }
}

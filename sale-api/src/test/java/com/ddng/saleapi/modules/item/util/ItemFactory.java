package com.ddng.saleapi.modules.item.util;

import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.item.domain.ItemType;
import com.ddng.saleapi.modules.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h1>상품 생성 팩토리</h1>
 */
@Component
@Transactional
@RequiredArgsConstructor
public class ItemFactory
{
    private final ItemRepository itemRepository;

    /**
     * 상품을 생성한다
     * @param name
     * @param type
     * @param barcode
     * @param price
     * @param itemQuantity
     * @param stamp
     * @return
     */
    public Item createItem(String name, ItemType type, String barcode, int price, int itemQuantity, boolean stamp)
    {
        Item item = new Item();
        item.setName(name);
        item.setType(type);
        item.setBarcode(barcode);
        item.setStamp(stamp);
        item.setPrice(price);
        item.setItemQuantity(itemQuantity);

        return itemRepository.save(item);
    }
}

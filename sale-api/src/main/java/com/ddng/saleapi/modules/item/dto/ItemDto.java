package com.ddng.saleapi.modules.item.dto;

import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.item.domain.ItemType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ItemDto
{
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter @Setter
    public static class Response
    {
        private Long id;
        private String name;
        private ItemType type;
        private String barcode;
        private int price;
        private int itemQuantity;

        private String typeName;

        public Response(Item item)
        {
            this.id = item.getId();
            this.name = item.getName();
            this.type = item.getType();
            this.barcode = item.getBarcode();
            this.price = item.getPrice();
            this.itemQuantity = item.getItemQuantity();

            this.typeName = item.getType().getName();
        }
    }
}

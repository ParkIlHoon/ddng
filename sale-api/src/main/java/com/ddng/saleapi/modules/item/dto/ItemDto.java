package com.ddng.saleapi.modules.item.dto;

import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.item.domain.ItemType;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ItemDto
{
    /**
     * <h1>응답용 상품 DTO</h1>
     */
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

    /**
     * <h1>Post 요청 처리용 상품 DTO</h1>
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter @Setter @Builder
    public static class Post
    {
        @NotEmpty
        private String name;
        @NotNull
        private ItemType type;
        private String barcode;
        private int price;
        private int itemQuantity;
    }
}

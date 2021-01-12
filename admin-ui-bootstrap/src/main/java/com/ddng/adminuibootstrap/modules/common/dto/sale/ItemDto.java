package com.ddng.adminuibootstrap.modules.common.dto.sale;

import lombok.*;

/**
 * <h1>ddng-sale-api 상품 DTO</h1>
 */
@Data
@NoArgsConstructor @AllArgsConstructor
public class ItemDto
{
    private Long id;
    private String name;
    private String type;
    private String barcode;
    private int price;
    private String unit;
    private int itemQuantity;
    private boolean stamp;
    private String itemImg;

    private String typeName;
}

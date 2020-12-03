package com.ddng.adminuibootstrap.modules.item.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ItemDto
{
    private Long id;
    private String name;
    private String type;
    private String barcode;
    private int price;
    private int itemQuantity;
    private boolean stamp;
    private String itemImg;

    private String typeName;
}

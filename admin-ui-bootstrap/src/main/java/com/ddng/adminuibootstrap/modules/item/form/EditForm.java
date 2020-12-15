package com.ddng.adminuibootstrap.modules.item.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * <h1>상품 수정 폼 클래스</h1>
 */
@Data
@NoArgsConstructor @AllArgsConstructor
public class EditForm
{
    /**
     * 상품 아이디
     */
    @NotBlank
    private Long id;

    /**
     * 상품명
     */
    @NotBlank(message = "상품명은 반드시 입력해야합니다.")
    @Length(min = 1, max = 200, message = "상품명은 1~200자이어야 합니다.")
    private String name;

    /**
     * 상품 이미지
     */
    private String itemImg;

    /**
     * 상품 바코드
     */
    private String barcode;

    /**
     * 상품 종류
     */
    @NotBlank(message = "상품 종류는 반드시 선택해야합니다.")
    private String type;

    /**
     * 상품 판매가
     */
    @NotBlank(message = "상품 가격은 반드시 입력해야합니다.")
    @Min(value = 0, message = "상품 가격은 최소 0원 입니다.")
    private int price;

    /**
     * 상품 단위
     */
    private String unit;

    /**
     * 상품 재고 수량
     */
    private int itemQuantity;

    /**
     * 적립 여부
     */
    private boolean stamp;
}

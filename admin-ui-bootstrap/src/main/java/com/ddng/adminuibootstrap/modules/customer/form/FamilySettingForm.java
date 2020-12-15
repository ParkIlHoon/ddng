package com.ddng.adminuibootstrap.modules.customer.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <h1>가족 설정 폼 클래스</h1>
 */
@Data
@NoArgsConstructor @AllArgsConstructor
public class FamilySettingForm
{
    /**
     * 가족 아이디
     */
    @NotBlank
    private Long id;

    /**
     * 가족 이름
     */
    @NotBlank(message = "가족 이름은 반드시 입력해야합니다.")
    @Length(min = 1, max = 100, message = "가족 이름은 1~100자이어야 합니다.")
    private String name;
}

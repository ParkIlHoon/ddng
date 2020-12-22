package com.ddng.adminuibootstrap.modules.customer.form;

import com.ddng.adminuibootstrap.modules.customer.dto.FamilyDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @NotNull
    private Long id;

    /**
     * 가족 이름
     */
    @NotBlank(message = "가족 이름은 반드시 입력해야합니다.")
    @Length(min = 1, max = 100, message = "가족 이름은 1~100자이어야 합니다.")
    private String name;

    public FamilySettingForm(FamilyDto familyDto)
    {
        this.id = familyDto.getId();
        this.name = familyDto.getName();
    }
}

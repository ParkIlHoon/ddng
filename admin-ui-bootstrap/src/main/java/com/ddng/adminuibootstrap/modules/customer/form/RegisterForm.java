package com.ddng.adminuibootstrap.modules.customer.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * <h1>고객 등록 폼 클래스</h1>
 */
@Data
@NoArgsConstructor @AllArgsConstructor
public class RegisterForm
{
    /**
     * 고객명
     */
    @NotBlank(message = "고객명은 반드시 입력해야합니다.")
    @Length(min = 1, max = 100, message = "고객명은 1~100자이어야 합니다.")
    private String name;

    /**
     * 고객 종류
     */
    @NotBlank(message = "고객 종류는 반드시 입력해야합니다.")
    private String type;

    /**
     * 가족 아이디
     */
    private Long familyId;

    /**
     * 전화 번호
     */
    @NotBlank(message = "전화번호는 반드시 입력해야합니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바르지 않은 전화번호입니다.")
    private String telNo;

    /**
     * 비고
     */
    private String bigo;

    /**
     * 프로필 이미지
     */
    private String profileImg;
}

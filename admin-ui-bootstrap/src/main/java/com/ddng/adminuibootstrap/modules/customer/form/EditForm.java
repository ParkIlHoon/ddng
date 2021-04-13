package com.ddng.adminuibootstrap.modules.customer.form;

import com.ddng.adminuibootstrap.modules.common.dto.customer.CustomerDto;
import com.ddng.adminuibootstrap.modules.common.dto.customer.CustomerTagDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * <h1>고객 수정 폼 클래스</h1>
 */
@Data
@NoArgsConstructor @AllArgsConstructor
public class EditForm
{
    /**
     * 고객 아이디
     */
    @NotNull
    private Long id;

    /**
     * 프로필 이미지
     */
    private String profileImg;

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
     * 가족
     */
    private String familyString;

    /**
     * 전화 번호
     */
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바르지 않은 전화번호입니다.")
    private String telNo;

    /**
     * 가입일
     */
    @DateTimeFormat(pattern = "yyyyMMdd")
    @PastOrPresent(message = "오늘 이후의 날짜는 입력할 수 없습니다.")
    private LocalDateTime joinDate;

    /**
     * 태그
     */
    private Set<CustomerTagDto> tags = new HashSet<>();

    /**
     * 비고
     */
    private String bigo;

    public EditForm(CustomerDto dto)
    {
        this.id = dto.getId();
        this.profileImg = dto.getProfileImg();
        this.name = dto.getName();
        this.type = dto.getType();
        this.familyString = dto.getFamilyString();
        this.telNo = dto.getTelNo();
        this.joinDate = dto.getJoinDate();
        this.tags = dto.getTags();
        this.bigo = dto.getBigo();
    }
}

package com.ddng.adminuibootstrap.modules.schedules.form;

import com.ddng.adminuibootstrap.modules.common.dto.schedule.ScheduleTagDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * <h1>스케쥴 등록/수정 폼 클래스</h1>
 */
@Data
@NoArgsConstructor @AllArgsConstructor
public class ScheduleForm
{
    /**
     * 스케쥴 아이디
     */
    private Long id;

    /**
     * 스케쥴 이름
     */
    @NotBlank(message = "스케쥴 이름은 반드시 입력해야합니다.")
    @Length(min = 1, max = 200, message = "스케쥴 이름은 1~200자이어야 합니다.")
    private String name;

    /**
     * 스케쥴 타입
     */
    @NotBlank(message = "스케쥴 종류는 반드시 선택해야합니다.")
    private String type;

    /**
     * 스케쥴 시작일시
     */
    @NotNull(message = "스케쥴 시작일시는 반드시 입력해야합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;

    /**
     * 스케쥴 종료일시
     */
    @NotNull(message = "스케쥴 종료일시는 반드시 입력해야합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;

    /**
     * 하루종일 여부
     */
    private boolean allDay;

    /**
     * 고객 아이디
     */
    private Long customerId;

    /**
     * 사용자 아이디
     */
    private Long userId;

    /**
     * 스케쥴 태그
     */
    private Set<ScheduleTagDto> tags = new HashSet<>();

    /**
     * 스케쥴 비고
     */
    private String bigo;
}

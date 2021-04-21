package com.ddng.adminuibootstrap.modules.canvas.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>캔버스 등록 폼 클래스</h1>
 */
@Data
@NoArgsConstructor @AllArgsConstructor
public class CanvasRegisterForm
{

    @NotBlank(message = "제목을 입력해주세요.")
    @Length(min = 1, max = 100, message = "제목은 1~100자이어야 합니다.")
    private String title;

    @NotNull(message = "상단 고정 여부를 지정해주세요.")
    private boolean topFixed = false;

    @NotNull(message = "캔버스에 첨부할 이미지를 선택해주세요.")
    private MultipartFile canvasImage;

    private List<String> tags = new ArrayList<>();
}

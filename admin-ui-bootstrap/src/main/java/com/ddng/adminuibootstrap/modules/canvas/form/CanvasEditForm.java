package com.ddng.adminuibootstrap.modules.canvas.form;

import com.ddng.adminuibootstrap.modules.common.dto.utils.canvas.CanvasDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor @AllArgsConstructor
public class CanvasEditForm
{
    private Long id;

    @NotBlank(message = "제목을 입력해주세요.")
    @Length(min = 1, max = 100, message = "제목은 1~100자이어야 합니다.")
    private String title;

    @NotNull(message = "상단 고정 여부를 지정해주세요.")
    private boolean topFixed = false;

    private String filePath;

    private List<String> tags = new ArrayList<>();

    public CanvasEditForm(CanvasDto.Response canvasDto)
    {
        this.id = canvasDto.getId();
        this.title = canvasDto.getTitle();
        this.topFixed = canvasDto.isTopFixed();
        this.filePath = "/canvas-management/canvas/image/" + canvasDto.getFilePath();
        this.tags = canvasDto.getTags().stream().map(t -> t.getTitle()).collect(Collectors.toList());
    }
}

package com.ddng.adminuibootstrap.modules.common.dto.utils.canvas;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class CanvasTagDto
{
    private Long id;
    private String title;

    public CanvasTagDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}

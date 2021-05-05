package com.ddng.userui.modules.common.dto.canvas;

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

    public CanvasTagDto(String title) {
        this.title = title;
    }
}

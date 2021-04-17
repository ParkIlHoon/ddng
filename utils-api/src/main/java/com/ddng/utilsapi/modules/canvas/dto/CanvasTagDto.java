package com.ddng.utilsapi.modules.canvas.dto;

import com.ddng.utilsapi.modules.canvas.domain.CanvasTag;
import com.querydsl.core.annotations.QueryProjection;
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

    @QueryProjection
    public CanvasTagDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public CanvasTagDto(CanvasTag tag) {
        this.id = tag.getId();
        this.title = tag.getTitle();
    }
}

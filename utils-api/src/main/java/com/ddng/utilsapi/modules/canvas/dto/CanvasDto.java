package com.ddng.utilsapi.modules.canvas.dto;


import com.ddng.utilsapi.modules.canvas.domain.CanvasTag;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CanvasDto
{
    /**
     * <h1>응답용 고객 DTO</h1>
     */
    @Data
    @NoArgsConstructor
    @ApiModel("캔버스 조회용 DTO")
    public static class Response
    {
        private Long id;
        private String title;
        private boolean isTopFixed;
        private LocalDateTime createDate;
        private Set<CanvasTagDto> tags = new HashSet<>();

        @QueryProjection
        public Response(Long id, String title, boolean isTopFixed, LocalDateTime createDate, Set<CanvasTag> tags)
        {
            this.id = id;
            this.title = title;
            this.isTopFixed = isTopFixed;
            this.createDate = createDate;
            this.tags = tags.stream().map(t -> new CanvasTagDto(t)).collect(Collectors.toSet());
        }
    }
}

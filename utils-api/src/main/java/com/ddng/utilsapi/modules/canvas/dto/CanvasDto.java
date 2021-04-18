package com.ddng.utilsapi.modules.canvas.dto;


import com.ddng.utilsapi.modules.canvas.domain.Canvas;
import com.ddng.utilsapi.modules.canvas.domain.CanvasTag;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
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
        private String filePath;
        private boolean isTopFixed;
        private LocalDateTime createDate;
        private Set<CanvasTagDto> tags = new HashSet<>();

        @QueryProjection
        public Response(Long id, String title, String filePath, boolean isTopFixed, LocalDateTime createDate, Set<CanvasTag> tags)
        {
            this.id = id;
            this.title = title;
            this.filePath = filePath;
            this.isTopFixed = isTopFixed;
            this.createDate = createDate;
            this.tags = tags.stream().map(CanvasTagDto::new).collect(Collectors.toSet());
        }

        public Response(Canvas canvas) {
            this.id = canvas.getId();
            this.title = canvas.getTitle();
            this.filePath = canvas.getFilePath();
            this.isTopFixed = canvas.isTopFixed();
            this.createDate = canvas.getCreateDate();
            this.tags = canvas.getTags().stream().map(CanvasTagDto::new).collect(Collectors.toSet());
        }
    }

    @Data
    @NoArgsConstructor @AllArgsConstructor
    public static class Create
    {
        private String title;
        private String filePath;
        private boolean isTopFixed;
        private Set<CanvasTagDto> tags = new HashSet<>();
    }

    @Data
    @NoArgsConstructor @AllArgsConstructor
    public static class Update
    {
        private String title;
        private String filePath;
        private boolean isTopFixed;
    }
}

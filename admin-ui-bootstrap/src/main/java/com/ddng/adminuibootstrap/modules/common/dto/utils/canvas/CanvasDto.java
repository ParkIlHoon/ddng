package com.ddng.adminuibootstrap.modules.common.dto.utils.canvas;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class CanvasDto
{
    /**
     * <h1>응답용 고객 DTO</h1>
     */
    @Data
    @NoArgsConstructor
    public static class Response
    {
        private Long id;
        private String title;
        private String filePath;
        private boolean isTopFixed;
        private LocalDateTime createDate;
        private Set<CanvasTagDto> tags = new HashSet<>();
    }

    @Data
    @NoArgsConstructor @AllArgsConstructor
    public static class Create
    {
        private String title;
        private String filePath;
        private boolean isTopFixed;
        private Set<String> tags = new HashSet<>();
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

package com.ddng.adminuibootstrap.modules.common.dto.utils.file;

import lombok.Data;

@Data
public class FileUploadRespDto {
    private int code = 0;
    private String message = "정상적으로 업로드되었습니다.";
    private String fileUrl;
    private String thumbnailUrl;
}

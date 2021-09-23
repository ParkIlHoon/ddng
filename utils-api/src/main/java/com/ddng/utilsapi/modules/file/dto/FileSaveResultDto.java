package com.ddng.utilsapi.modules.file.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FileSaveResultDto {
    String fileUrl;
    String filePath;
    String thumbnailUrl;
    String thumbnailPath;
}

package com.ddng.adminuibootstrap.modules.canvas.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadForm {
    @NotNull(message = "업로드할 파일이 비어있습니다.")
    private MultipartFile file;
    private boolean createThumbnail = false;
    private int thumbnailWidth = 200;
}
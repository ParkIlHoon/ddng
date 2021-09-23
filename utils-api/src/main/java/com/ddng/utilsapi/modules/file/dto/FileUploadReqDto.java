package com.ddng.utilsapi.modules.file.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "파일 업로드 요청 DTO", description = "파일 업로드를 위한 DTO 입니다.")
public class FileUploadReqDto {
    @ApiModelProperty(value = "업로드할 파일", required = true)
    @NotNull(message = "업로드할 파일이 비어있습니다.")
    private MultipartFile file;
    @ApiModelProperty(value = "썸네일 생성 여부", example = "true")
    private boolean createThumbnail = false;
    @ApiModelProperty(value = "썸네일 너비", example = "200")
    private int thumbnailWidth = 200;
}
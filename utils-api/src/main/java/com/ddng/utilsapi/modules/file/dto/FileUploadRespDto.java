package com.ddng.utilsapi.modules.file.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.modelmapper.internal.Errors;
import org.modelmapper.spi.ErrorMessage;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@ApiModel(value = "파일 업로드 응답 DTO", description = "파일 업로드 결과를 응답하는 DTO 입니다.")
public class FileUploadRespDto {
    @NotNull
    @ApiModelProperty(value = "결과 코드", example = "0")
    private int code = 0;
    @NotEmpty
    @ApiModelProperty(value = "결과 메시지", example = "정상적으로 업로드되었습니다.")
    private String message = "정상적으로 업로드되었습니다.";
    @ApiModelProperty(value = "업로드된 파일 URL", example = "files/customer-profile/3c43680c-9174-454a-811f-fd55dc0d9cca")
    private String fileUrl;
    @ApiModelProperty(value = "썸네일 파일 URL", example = "files/customer-profile/thumbnail/3c43680c-9174-454a-811f-fd55dc0d9cca")
    private String thumbnailUrl;

    public FileUploadRespDto successToUpload(String fileUrl) {
        this.fileUrl = fileUrl;
        return this;
    }

    public FileUploadRespDto successToUpload(String fileUrl, String thumbnailUrl) {
        this.fileUrl = fileUrl;
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }

    public FileUploadRespDto failToUpload(Errors errors) {
        this.code = 1000;
        StringBuilder sb = new StringBuilder();
        String lineSeparator = System.getProperty("line.separator");
        for (ErrorMessage message : errors.getMessages()) {
            sb.append(message.getMessage());
            sb.append(lineSeparator);
        }
        this.message = sb.toString();
        return this;
    }

    /**
     * 업로드에 실패했을 경우 응답 DTO를 세팅합니다.
     *
     * @param message 에러 메시지
     * @return 응답 DTO
     */
    public FileUploadRespDto failToUpload(String message) {
        this.code = 1001;
        this.message = message;
        return this;
    }
}

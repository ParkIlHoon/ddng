package com.ddng.utilsapi.modules.file.controller;

import com.ddng.utilsapi.modules.file.domain.FileMetaData;
import com.ddng.utilsapi.modules.file.dto.FileSaveResultDto;
import com.ddng.utilsapi.modules.file.dto.FileUploadReqDto;
import com.ddng.utilsapi.modules.file.dto.FileUploadRespDto;
import com.ddng.utilsapi.modules.file.service.FileService;
import com.ddng.utilsapi.modules.file.service.FileMetaDataService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.Errors;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping("/files")
@Api(value = "파일 업로드와 다운로드를 처리합니다.")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final FileMetaDataService fileMetaDataService;

    private static final String REQUEST_PREFIX = "files";

    @PostMapping("/{category}")
    @ApiOperation(value = "파일 업로드", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200,
                    message = "업로드 성공",
                    response = FileUploadRespDto.class,
                    examples = @Example(value = {
                            @ExampleProperty(mediaType = MediaType.APPLICATION_JSON_VALUE, value = "{\"code\" : 0, \"message\" : \"정상적으로 업로드되었습니다.\", \"fileUrl\" : \"files/customer-profile/3c43680c-9174-454a-811f-fd55dc0d9cca.png\", \"thumbnailUrl\" : \"files/customer-profile/thumbnail/3c43680c-9174-454a-811f-fd55dc0d9cca.png\" }")
                    })),
            @ApiResponse(code = 400, message = "업로드를 실패했습니다."),
            @ApiResponse(code = 500, message = "업로드를 실패했습니다.")
    })
    public ResponseEntity<FileUploadRespDto> uploadFile (@ApiParam(value = "파일 카테고리")
                                                         @PathVariable("category") String category,
                                                         @ApiParam(value = "파일 업로드 요청 DTO")
                                                         @Valid
                                                         @ModelAttribute FileUploadReqDto fileUploadReqDto,
                                                         @ApiParam(hidden = true) Errors errors) throws IOException {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(new FileUploadRespDto().failToUpload(errors));
        }

        FileSaveResultDto fileSaveResultDto = fileService.saveFile(category, fileUploadReqDto.getFile(), fileUploadReqDto.getThumbnailWidth());
        if (fileSaveResultDto == null) {
            return ResponseEntity.badRequest().build();
        }

        fileMetaDataService.saveMetaData(fileSaveResultDto.getFileUrl(), fileSaveResultDto.getFilePath());

        if (!StringUtils.isEmpty(fileSaveResultDto.getThumbnailPath())) {
            fileMetaDataService.saveMetaData(fileSaveResultDto.getThumbnailUrl(), fileSaveResultDto.getThumbnailPath());
        }

        return ResponseEntity.ok(new FileUploadRespDto().successToUpload(fileSaveResultDto.getFileUrl(), fileSaveResultDto.getThumbnailUrl()));
    }

    @GetMapping("/{category}/{uuid}")
    @ApiOperation(value = "파일 다운로드", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.ALL_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "다운로드 성공"),
            @ApiResponse(code = 404, message = "파일을 찾을 수 없습니다.")
    })
    public ResponseEntity<StreamingResponseBody> downloadFile (@ApiParam(value = "카테고리")
                                                               @PathVariable("category") String category,
                                                               @ApiParam(value = "파일 uuid")
                                                               @PathVariable("uuid") String uuid) throws IOException {
        FileMetaData metaData = fileMetaDataService.getMetaData(REQUEST_PREFIX + "/" + category + "/" + uuid);
        InputStream is = fileService.readStream(metaData.getFilePath());
        StreamingResponseBody streamingResponseBody = os -> FileCopyUtils.copy(is, os);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(streamingResponseBody);
    }

    @GetMapping("/{category}/thumbnail/{uuid}")
    @ApiOperation(value = "썸네일 다운로드", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.ALL_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "다운로드 성공"),
            @ApiResponse(code = 404, message = "파일을 찾을 수 없습니다.")
    })
    public ResponseEntity<StreamingResponseBody> downloadThumbnail (@ApiParam(value = "카테고리")
                                                               @PathVariable("category") String category,
                                                               @ApiParam(value = "파일 uuid")
                                                               @PathVariable("uuid") String uuid) throws IOException {
        FileMetaData metaData = fileMetaDataService.getMetaData(REQUEST_PREFIX + "/" + category + "/thumbnail/" + uuid);
        InputStream is = fileService.readStream(metaData.getFilePath());
        StreamingResponseBody streamingResponseBody = os -> FileCopyUtils.copy(is, os);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(streamingResponseBody);
    }

    @DeleteMapping("/{category}/{uuid}")
    @ApiOperation(value = "파일 삭제", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "다운로드 성공"),
            @ApiResponse(code = 404, message = "파일을 찾을 수 없습니다."),
            @ApiResponse(code = 500, message = "파일을 삭제하지 못했습니다.")
    })
    public ResponseEntity deleteFile (@ApiParam(value = "카테고리")
                                      @PathVariable("category") String category,
                                      @ApiParam(value = "파일 uuid")
                                      @PathVariable("uuid") String uuid) throws IOException {
        FileMetaData metaData = fileMetaDataService.getMetaData(REQUEST_PREFIX + "/" + category + "/" + uuid);
        boolean isRemoved = fileService.removeFile(metaData.getFilePath());
        if (!isRemoved) {
            fileMetaDataService.deleteMetaData(metaData.getId());
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

}

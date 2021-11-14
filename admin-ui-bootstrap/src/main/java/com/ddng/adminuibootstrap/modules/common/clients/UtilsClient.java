package com.ddng.adminuibootstrap.modules.common.clients;

import com.ddng.adminuibootstrap.modules.canvas.form.FileUploadForm;
import com.ddng.adminuibootstrap.modules.common.dto.FeignPageImpl;
import com.ddng.adminuibootstrap.modules.common.dto.utils.canvas.CanvasDto;
import com.ddng.adminuibootstrap.modules.common.dto.utils.canvas.CanvasTagDto;
import com.ddng.adminuibootstrap.modules.common.dto.utils.file.FileUploadRespDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;
import java.util.Set;

@FeignClient(name = "ddng-utils-api", fallback = UtilsClientFallback.class)
public interface UtilsClient
{
    String CANVAS_API = "/canvas";
    String FILE_API = "/files";

    /**
     * 태그에 해당하는 캔버스 목록을 조회하는 API를 호출한다.
     * @param tags 조회할 태그 목록
     * @param page 조회할 페이지
     * @param size 조회할 건수
     * @return 태그에 해당하는 캔버스 목록
     */
    @GetMapping(CANVAS_API)
    FeignPageImpl<CanvasDto.Response> getCanvasWithPage(@RequestParam("tags") List<String> tags,
                                               @RequestParam("page") int page,
                                               @RequestParam("size") int size);

    /**
     * 캔버스를 조회하는 API를 호출한다.
     * @param canvasId 조회할 캔버스 아이디
     * @return 아이디에 해당하는 캔버스
     */
    @GetMapping(CANVAS_API + "/{canvasId}")
    CanvasDto.Response getCanvas(@PathVariable("canvasId") Long canvasId);

    /**
     * 캔버스 태그 목록을 조회하는 API를 호출한다.
     * @param onlyUsed 사용중인 태그만 조회할지 여부
     * @return 캔버스 태그 목록
     */
    @GetMapping(CANVAS_API + "/tags")
    List<CanvasTagDto> getCanvasTags(@RequestParam("onlyUsed") boolean onlyUsed);

    /**
     * 캔버스를 생성하는 API를 호출한다.
     * @param dto 새로 생성할 캔버스 정보
     */
    @PostMapping(CANVAS_API)
    ResponseEntity createCanvas(@RequestBody CanvasDto.Create dto);

    /**
     * 캔버스를 수정하는 API를 호출한다.
     * @param id 수정할 캔버스 아이디
     * @param dto 수정할 캔버스 정보
     */
    @PutMapping(CANVAS_API + "/{canvasId}")
    ResponseEntity updateCanvas(@PathVariable("canvasId") Long id,
                      @RequestBody CanvasDto.Update dto);

    /**
     * 캔버스를 삭제하는 API를 호출한다.
     * @param id 삭제할 캔버스 아이디
     */
    @DeleteMapping(CANVAS_API + "/{canvasId}")
    ResponseEntity deleteCanvas(@PathVariable("canvasId") Long id);


    /**
     * 캔버스의 태그를 조회하는 API를 호출한다.
     * @param id 캔버스 아이디
     * @return 해당 캔버스의 태그 목록
     */
    @GetMapping(CANVAS_API + "/{canvasId}/tags")
    Set<CanvasTagDto> getCanvasTags(@PathVariable("canvasId") Long id);

    /**
     * 캔버스에 태그를 추가하는 API를 호출한다.
     * @param id 캔버스 아이디
     * @param title 추가할 태그 타이틀
     */
    @PostMapping(CANVAS_API + "/{canvasId}/tags")
    ResponseEntity addCanvasTag(@PathVariable("canvasId") Long id, @RequestBody String title);

    /**
     * 캔버스의 태그를 제거하는 API를 호출한다.
     * @param id 캔버스 아이디
     * @param title 제거할 태그 타이틀
     */
    @DeleteMapping(CANVAS_API + "/{canvasId}/tags")
    ResponseEntity removeCanvasTag(@PathVariable("canvasId") Long id, @RequestBody String title);

    /**
     * 파일 업로드 API를 호출한다.
     * @param category 카테고리
     * @param fileUploadForm 업로드 정보
     */
    @PostMapping(value = FILE_API + "/{category}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FileUploadRespDto uploadFile(@PathVariable("category") String category, @RequestBody FileUploadForm fileUploadForm);

    /**
     * 파일 삭제 API를 호출한다.
     * @param category 카테고리
     * @param uuid 파일 UUID
     */
    @DeleteMapping(FILE_API + "/{category}/{uuid}")
    ResponseEntity deleteFile(@PathVariable("category") String category, @PathVariable("uuid") String uuid);
}

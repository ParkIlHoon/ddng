package com.ddng.userui.modules.common.clients;

import com.ddng.userui.modules.common.dto.FeignPageImpl;
import com.ddng.userui.modules.common.dto.canvas.CanvasDto;
import com.ddng.userui.modules.common.dto.canvas.CanvasTagDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@FeignClient(name = "ddng-utils-api")
public interface UtilsClient
{
    String CANVAS_API = "/canvas";

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
     * 캔버스의 태그를 조회하는 API를 호출한다.
     * @param id 캔버스 아이디
     * @return 해당 캔버스의 태그 목록
     */
    @GetMapping(CANVAS_API + "/{canvasId}/tags")
    Set<CanvasTagDto> getCanvasTags(@PathVariable("canvasId") Long id);
}

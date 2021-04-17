package com.ddng.utilsapi.modules.canvas.controller;

import com.ddng.utilsapi.modules.canvas.dto.CanvasDto;
import com.ddng.utilsapi.modules.canvas.service.CanvasService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(value = "캔버스 컨트롤러")
@RequestMapping("/canvas")
@RequiredArgsConstructor
public class CanvasController
{
    private final CanvasService canvasService;

    @GetMapping
    @ApiOperation(value = "캔버스 목록 조회", notes = "태그에 해당하는 캔버스 목록을 조회합니다.")
    public ResponseEntity getCanvasWithPage(@ApiParam(value = "검색 태그", required = false) String[] tags,
                                            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable)
    {
        Page<CanvasDto.Response> canvas = canvasService.findCanvas(tags, pageable);
        return ResponseEntity.ok(canvas);
    }
}

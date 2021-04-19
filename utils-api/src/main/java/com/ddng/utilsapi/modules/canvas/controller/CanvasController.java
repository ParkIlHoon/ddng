package com.ddng.utilsapi.modules.canvas.controller;

import com.ddng.utilsapi.modules.canvas.domain.Canvas;
import com.ddng.utilsapi.modules.canvas.dto.CanvasDto;
import com.ddng.utilsapi.modules.canvas.dto.CanvasTagDto;
import com.ddng.utilsapi.modules.canvas.service.CanvasService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@RestController
@Api(value = "캔버스 컨트롤러")
@RequestMapping("/canvas")
@RequiredArgsConstructor
public class CanvasController
{
    private final CanvasService canvasService;

    @GetMapping
    @ApiOperation(value = "캔버스 목록 조회", notes = "태그에 해당하는 캔버스 목록을 조회합니다.")
    public ResponseEntity getCanvasWithPage(@ApiParam(value = "검색 태그", required = false) @RequestParam("tags") List<String> tags,
                                            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable)
    {
        Page<CanvasDto.Response> canvas = canvasService.findCanvas(tags, pageable);
        return ResponseEntity.ok(canvas);
    }

    @GetMapping("/tags")
    @ApiOperation(value = "캔버스 태그 조회", notes = "캔버스의 태그 목록을 조회합니다.")
    public ResponseEntity getCanvasTags(@ApiParam(value = "사용중인 태그만 조회할지 여부", required = true) boolean onlyUsed)
    {
        List<CanvasTagDto> tags;
        if (onlyUsed)
        {
            tags = canvasService.getUsedCanvasTags();
        }
        else
        {
            tags = canvasService.getAllCanvasTags();
        }
        return ResponseEntity.ok(tags);
    }

    @PostMapping
    @ApiOperation(value = "캔버스 생성", notes = "새로운 캔버스를 생성합니다.")
    public ResponseEntity createCanvas(@RequestBody @Valid CanvasDto.Create dto,
                                       Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.badRequest().build();
        }

        Canvas canvas = canvasService.createCanvas(dto);
        WebMvcLinkBuilder builder = linkTo(CanvasController.class).slash(canvas.getId());
        return ResponseEntity.created(builder.toUri()).build();
    }

    @PutMapping("/{canvasId}")
    @ApiOperation(value = "캔버스 수정", notes = "캔버스 내용을 변경합니다.")
    public ResponseEntity updateCanvas(@PathVariable("canvasId") Long id,
                                       @RequestBody @Valid CanvasDto.Update dto,
                                       Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.badRequest().build();
        }

        Optional<Canvas> canvasById = canvasService.findCanvasById(id);
        if(canvasById.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            Canvas canvas = canvasById.get();
            Canvas updated = canvasService.updateCanvas(canvas, dto);
            CanvasDto.Response response = new CanvasDto.Response(updated);
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/{canvasId}")
    public ResponseEntity deleteCanvas(@PathVariable("canvasId") Long id)
    {
        Optional<Canvas> canvasById = canvasService.findCanvasById(id);
        if(canvasById.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            Canvas canvas = canvasById.get();
            canvasService.deleteCanvas(canvas);
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/{canvasId}/tags")
    public ResponseEntity getCanvasTags(@PathVariable("canvasId") Long id)
    {
        Optional<Canvas> canvasById = canvasService.findCanvasById(id);
        if(canvasById.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            Canvas canvas = canvasById.get();
            Set<CanvasTagDto> collect = canvas.getTags().stream().map(CanvasTagDto::new).collect(Collectors.toSet());
            return ResponseEntity.ok(collect);
        }
    }

    @PostMapping("/{canvasId}/tags")
    public ResponseEntity addCanvasTag(@PathVariable("canvasId") Long id,
                                       @RequestBody @Valid String title,
                                       Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.badRequest().build();
        }

        Optional<Canvas> canvasById = canvasService.findCanvasById(id);
        if(canvasById.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            Canvas canvas = canvasById.get();
            canvasService.addTag(canvas, title);
            return ResponseEntity.ok().build();
        }
    }

    @DeleteMapping("/{canvasId}/tags")
    public ResponseEntity removeCanvasTag(@PathVariable("canvasId") Long id,
                                           @RequestBody @Valid String title,
                                           Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.badRequest().build();
        }

        Optional<Canvas> canvasById = canvasService.findCanvasById(id);
        if(canvasById.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }
        else
        {
            Canvas canvas = canvasById.get();
            canvasService.removeTag(canvas, title);
            return ResponseEntity.ok().build();
        }
    }
}

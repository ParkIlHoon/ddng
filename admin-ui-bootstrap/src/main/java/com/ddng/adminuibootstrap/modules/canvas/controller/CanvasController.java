package com.ddng.adminuibootstrap.modules.canvas.controller;

import com.ddng.adminuibootstrap.modules.common.clients.UtilsClient;
import com.ddng.adminuibootstrap.modules.common.dto.FeignPageImpl;
import com.ddng.adminuibootstrap.modules.common.dto.utils.canvas.CanvasDto;
import com.ddng.adminuibootstrap.modules.common.dto.utils.canvas.CanvasTagDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/canvas-management")
@RequiredArgsConstructor
public class CanvasController
{
    private final UtilsClient utilsClient;

    @RequestMapping("/main")
    public String mainForm (Model model)
    {
        List<CanvasTagDto> canvasTags = utilsClient.getCanvasTags(false);

        model.addAttribute("canvasTags", canvasTags);
        return "canvas/main";
    }

    @GetMapping("/canvas")
    public ResponseEntity searchCanvasList(@RequestParam("tags") List<String> tags,
                                           @RequestParam("page") int page,
                                           @RequestParam("size") int size)
    {
        FeignPageImpl<CanvasDto.Response> canvasWithPage = utilsClient.getCanvasWithPage(tags, page, size);

        return ResponseEntity.ok(canvasWithPage);
    }
}

package com.ddng.userui.modules.canvas;

import com.ddng.userui.infra.properties.ServiceProperties;
import com.ddng.userui.modules.common.clients.UtilsClient;
import com.ddng.userui.modules.common.dto.FeignPageImpl;
import com.ddng.userui.modules.common.dto.canvas.CanvasDto;
import com.ddng.userui.modules.common.dto.canvas.CanvasTagDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CanvasController
{
    private final ServiceProperties serviceProperties;
    private static final String CANVAS_FILE_PATH = "canvas";
    private final UtilsClient utilsClient;

    @GetMapping("/canvas")
    public String main (Model model)
    {
        List<CanvasTagDto> canvasTags = utilsClient.getCanvasTags(true);

        model.addAttribute("canvasTags", canvasTags);
        model.addAttribute("selectTags", new ArrayList<>());
        model.addAttribute("cardList", new ArrayList<>());
        return "canvas/main";
    }

    @GetMapping("/canvas/search")
    public String searchListWithTags(@RequestParam("tags[]") List<String> tags,
                                     @PageableDefault Pageable pageable,
                                     Model model)
    {
        List<CanvasTagDto> canvasTags = utilsClient.getCanvasTags(true);
        FeignPageImpl<CanvasDto.Response> canvasWithPage = utilsClient.getCanvasWithPage(tags, pageable.getPageNumber(), pageable.getPageSize());

        model.addAttribute("canvasTags", canvasTags);
        model.addAttribute("selectTags", tags);
        model.addAttribute("cardList", canvasWithPage.getContent());
        return "canvas/main :: #canvas-card";
    }

    @GetMapping("/canvas/{id}")
    public String detail (@PathVariable("id") Long id, Model model)
    {
        CanvasDto.Response canvas = utilsClient.getCanvas(id);
        model.addAttribute("canvas", canvas);
        return "canvas/card-detail";
    }
}

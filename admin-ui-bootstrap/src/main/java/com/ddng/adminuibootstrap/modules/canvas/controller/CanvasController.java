package com.ddng.adminuibootstrap.modules.canvas.controller;

import com.ddng.adminuibootstrap.modules.canvas.form.CanvasRegisterForm;
import com.ddng.adminuibootstrap.modules.common.clients.UtilsClient;
import com.ddng.adminuibootstrap.modules.common.dto.FeignPageImpl;
import com.ddng.adminuibootstrap.modules.common.dto.utils.canvas.CanvasDto;
import com.ddng.adminuibootstrap.modules.common.dto.utils.canvas.CanvasTagDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <h1>홈페이지 관리 > 캔버스 관리 메뉴 컨트롤러</h1>
 */
@Controller
@RequestMapping("/canvas-management")
@RequiredArgsConstructor
public class CanvasController
{
    private final UtilsClient utilsClient;

    /**
     * 메인 화면 뷰 요청
     * @param model
     * @return
     */
    @RequestMapping
    public String mainForm (Model model)
    {
        List<CanvasTagDto> canvasTags = utilsClient.getCanvasTags(false);

        model.addAttribute("canvasTags", canvasTags);
        model.addAttribute("registerForm", new CanvasRegisterForm());
        return "canvas/main";
    }

    /**
     * 캔버스 목록 조회 요청
     * @param tags 조회할 태그 목록
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/canvas")
    public ResponseEntity searchCanvasList(@RequestParam("tags") List<String> tags,
                                           @RequestParam("page") int page,
                                           @RequestParam("size") int size)
    {
        FeignPageImpl<CanvasDto.Response> canvasWithPage = utilsClient.getCanvasWithPage(tags, page, size);

        return ResponseEntity.ok(canvasWithPage);
    }

    /**
     * 캔버스 생성 요청
     * @param form
     * @param errors
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/canvas")
    public String createCanvas(@Valid @ModelAttribute CanvasRegisterForm form,
                               Errors errors,
                               RedirectAttributes redirectAttributes)
    {
        if (errors.hasErrors())
        {
            redirectAttributes.addFlashAttribute("alertType", "danger");
            redirectAttributes.addFlashAttribute("message", "잘못된 요청입니다.");
        }

        //TODO 파일 저장
        MultipartFile canvasImage = form.getCanvasImage();

        // 캔버스 저장 처리
        CanvasDto.Create canvasDto = new CanvasDto.Create();
        canvasDto.setTitle(form.getTitle());
        canvasDto.setTopFixed(form.isTopFixed());
        canvasDto.setTags(new HashSet<>(form.getTags()));

        utilsClient.createCanvas(canvasDto);

        redirectAttributes.addFlashAttribute("alertType", "success");
        redirectAttributes.addFlashAttribute("message", "캔버스가 정상적으로 등록되었습니다.");

        return "redirect:/canvas-management";
    }
}

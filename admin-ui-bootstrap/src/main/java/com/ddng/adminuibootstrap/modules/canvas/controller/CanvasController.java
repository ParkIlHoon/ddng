package com.ddng.adminuibootstrap.modules.canvas.controller;

import com.ddng.adminuibootstrap.infra.properties.ServiceProperties;
import com.ddng.adminuibootstrap.modules.canvas.form.CanvasEditForm;
import com.ddng.adminuibootstrap.modules.canvas.form.CanvasRegisterForm;
import com.ddng.adminuibootstrap.modules.canvas.form.FileUploadForm;
import com.ddng.adminuibootstrap.modules.common.clients.UtilsClient;
import com.ddng.adminuibootstrap.modules.common.dto.FeignPageImpl;
import com.ddng.adminuibootstrap.modules.common.dto.utils.canvas.CanvasDto;
import com.ddng.adminuibootstrap.modules.common.dto.utils.canvas.CanvasTagDto;
import com.ddng.adminuibootstrap.modules.common.dto.utils.file.FileUploadRespDto;
import com.ddng.adminuibootstrap.modules.common.utils.AlertUtils;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;

/**
 * <h1>홈페이지 관리 > 캔버스 관리 메뉴 컨트롤러</h1>
 */
@Controller
@RequestMapping("/canvas-management")
@RequiredArgsConstructor
public class CanvasController {

    private final UtilsClient utilsClient;
    private final ServiceProperties serviceProperties;

    @Value("${service.gateway.utils}")
    public String utilsApiEndpoint;

    private static final String HOME_URL = "/canvas-management";

    /**
     * 메인 화면 뷰 요청
     *
     * @param model
     * @return
     */
    @RequestMapping
    public String mainForm(Model model) {
        List<CanvasTagDto> canvasTags = utilsClient.getCanvasTags(false);
        model.addAttribute("canvasTags", canvasTags);
        model.addAttribute("registerForm", new CanvasRegisterForm());
        return "canvas/main";
    }

    /**
     * 캔버스 목록 조회 요청
     *
     * @param tags 조회할 태그 목록
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/canvas")
    public ResponseEntity searchCanvasList(@RequestParam("tags") List<String> tags,
                                            @RequestParam("page") @Min(0) int page,
                                            @RequestParam("size") @Min(1) int size) {
        FeignPageImpl<CanvasDto.Response> canvasWithPage = utilsClient.getCanvasWithPage(tags, page, size);
        return ResponseEntity.ok(canvasWithPage);
    }

    /**
     * 캔버스 생성 요청
     *
     * @param form
     * @param errors
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/canvas")
    public String createCanvas(@Valid
                                @ModelAttribute CanvasRegisterForm form,
                                Errors errors,
                                RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            AlertUtils.alertFail(redirectAttributes, "잘못된 요청입니다.");
            return "redirect:" + HOME_URL;
        }

        FileUploadRespDto uploadRespDto = utilsClient.uploadFile("canvas", new FileUploadForm().setFile(form.getCanvasImage()).setCreateThumbnail(true).setThumbnailWidth(400));

        // 캔버스 저장 처리
        CanvasDto.Create canvasDto = new CanvasDto.Create();
        canvasDto.setTitle(form.getTitle());
        canvasDto.setTopFixed(form.isTopFixed());
        canvasDto.setTags(new HashSet<>(form.getTags()));
        canvasDto.setFilePath(utilsApiEndpoint + "/" + uploadRespDto.getFileUrl());
        canvasDto.setThumbnail(utilsApiEndpoint + "/" + uploadRespDto.getThumbnailUrl());

        ResponseEntity responseEntity = utilsClient.createCanvas(canvasDto);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            AlertUtils.alertSuccess(redirectAttributes, "캔버스가 정상적으로 등록되었습니다.");
        } else {
            AlertUtils.alertFail(redirectAttributes, "캔버스가 등록 중 오류가 발생했습니다.");
        }
        return "redirect:" + HOME_URL;
    }

    /**
     * 캔버스 뷰 요청
     *
     * @param canvasId 조회할 캔버스
     * @param model
     * @return
     */
    @GetMapping("/canvas/{canvasId}")
    public String getCanvas(@PathVariable("canvasId") Long canvasId,
                            Model model) {
        CanvasDto.Response canvas = utilsClient.getCanvas(canvasId);
        List<CanvasTagDto> canvasTags = utilsClient.getCanvasTags(false);

        model.addAttribute("canvasTags", canvasTags);
        model.addAttribute("canvasEditForm", new CanvasEditForm(canvas));
        return "canvas/view-canvas-modal :: #view-canvas-modal";
    }

    /**
     * 캔버스 정보를 수정한다
     *
     * @param canvasId
     * @param form
     * @param errors
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/canvas/update/{canvasId}")
    public String updateCanvas(@PathVariable("canvasId") Long canvasId,
                                @Valid
                                @ModelAttribute CanvasEditForm form,
                                Errors errors,
                                RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            AlertUtils.alertFail(redirectAttributes, "잘못된 요청입니다.");
            return "redirect:" + HOME_URL;
        }

        CanvasDto.Update updateDto = new CanvasDto.Update();
        updateDto.setTitle(form.getTitle());
        updateDto.setTopFixed(form.isTopFixed());

        ResponseEntity responseEntity = utilsClient.updateCanvas(canvasId, updateDto);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            AlertUtils.alertSuccess(redirectAttributes, "정상적으로 수정되었습니다.");
        } else {
            AlertUtils.alertFail(redirectAttributes, "수정 중 오류가 발생해 수정하지 못했습니다.");
        }
        return "redirect:" + HOME_URL;
    }

    /**
     * 캔버스 태그를 추가한다.
     *
     * @param canvasId
     * @param title
     * @return
     */
    @PostMapping("/canvas/{canvasId}/tags/add")
    public ResponseEntity customerTagAddAction(@PathVariable("canvasId") Long canvasId, String title) {
        return utilsClient.addCanvasTag(canvasId, title);
    }

    /**
     * 캔버스 태그를 제거한다.
     *
     * @param canvasId
     * @param title
     * @return
     */
    @PostMapping("/canvas/{canvasId}/tags/remove")
    public ResponseEntity customerTagRemoveAction(@PathVariable("canvasId") Long canvasId, String title) {
        return utilsClient.removeCanvasTag(canvasId, title);
    }

    /**
     * 캔버스를 삭제한다.
     *
     * @param canvasId
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/canvas/remove/{canvasId}")
    public String deleteCanvas(@PathVariable("canvasId") Long canvasId,
        RedirectAttributes redirectAttributes) {
        ResponseEntity responseEntity = utilsClient.deleteCanvas(canvasId);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            AlertUtils.alertSuccess(redirectAttributes, "정상적으로 삭제되었습니다.");
        } else {
            AlertUtils.alertFail(redirectAttributes, "오류가 발생해 삭제하지 못했습니다.");
        }
        return "redirect:" + HOME_URL;
    }
}

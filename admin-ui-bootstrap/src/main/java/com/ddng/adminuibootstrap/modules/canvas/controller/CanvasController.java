package com.ddng.adminuibootstrap.modules.canvas.controller;

import com.ddng.adminuibootstrap.infra.properties.ServiceProperties;
import com.ddng.adminuibootstrap.modules.canvas.form.CanvasEditForm;
import com.ddng.adminuibootstrap.modules.canvas.form.CanvasRegisterForm;
import com.ddng.adminuibootstrap.modules.common.clients.UtilsClient;
import com.ddng.adminuibootstrap.modules.common.dto.FeignPageImpl;
import com.ddng.adminuibootstrap.modules.common.dto.utils.canvas.CanvasDto;
import com.ddng.adminuibootstrap.modules.common.dto.utils.canvas.CanvasTagDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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
    private final ServiceProperties serviceProperties;
    private static final String CANVAS_FILE_PATH = "canvas";

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
        String imagePath = saveCanvasImage(canvasImage);

        // 캔버스 저장 처리
        CanvasDto.Create canvasDto = new CanvasDto.Create();
        canvasDto.setTitle(form.getTitle());
        canvasDto.setTopFixed(form.isTopFixed());
        canvasDto.setTags(new HashSet<>(form.getTags()));
        canvasDto.setFilePath(imagePath);

        utilsClient.createCanvas(canvasDto);

        redirectAttributes.addFlashAttribute("alertType", "success");
        redirectAttributes.addFlashAttribute("message", "캔버스가 정상적으로 등록되었습니다.");

        return "redirect:/canvas-management";
    }

    /**
     * 캔버스 뷰 요청
     * @param canvasId 조회할 캔버스
     * @param model
     * @return
     */
    @GetMapping("/canvas/{canvasId}")
    public String getCanvas(@PathVariable("canvasId") Long canvasId,
                            Model model)
    {
        CanvasDto.Response canvas = utilsClient.getCanvas(canvasId);
        model.addAttribute("canvasEditForm", new CanvasEditForm(canvas));
        return "canvas/view-canvas-modal :: #view-canvas-modal";
    }

    /**
     * 캔버스 이미지 요청
     * @param folder
     * @param fileName
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/canvas/image/{folder}/{fileName}")
    public ResponseEntity getCanvasImage(@PathVariable("folder") String folder,
                                         @PathVariable("fileName") String fileName,
                                         HttpServletRequest request) throws Exception {
        String filePath = serviceProperties.getFile() + CANVAS_FILE_PATH + File.separator + StringUtils.cleanPath(folder + "/" + fileName);

        File file = new File(filePath);

        if (file.exists())
        {
            String contentType = "";
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            request.getServletContext().getMimeType(file.getAbsolutePath());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }

    private String saveCanvasImage(MultipartFile file)
    {
        String filePath = null;
        try
        {
            String name = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            filePath = uuid + File.separator + name;
            String dirPath = serviceProperties.getFile() + CANVAS_FILE_PATH + File.separator +  uuid;
            String fullPath = dirPath + File.separator +  name;

            File folder = new File(dirPath);
            if (!folder.exists())
            {
                folder.mkdirs();
            }

            File newFile = new File(fullPath);
            FileCopyUtils.copy(file.getBytes(), newFile);
        }
        catch (IOException e)
        {
            return null;
        }
        return filePath;
    }
}

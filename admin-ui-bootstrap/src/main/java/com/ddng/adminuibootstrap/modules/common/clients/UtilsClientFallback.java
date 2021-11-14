package com.ddng.adminuibootstrap.modules.common.clients;

import com.ddng.adminuibootstrap.modules.canvas.form.FileUploadForm;
import com.ddng.adminuibootstrap.modules.common.dto.FeignPageImpl;
import com.ddng.adminuibootstrap.modules.common.dto.utils.canvas.CanvasDto;
import com.ddng.adminuibootstrap.modules.common.dto.utils.canvas.CanvasTagDto;
import com.ddng.adminuibootstrap.modules.common.dto.utils.file.FileUploadRespDto;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UtilsClientFallback implements UtilsClient{

    @Override
    public FeignPageImpl<CanvasDto.Response> getCanvasWithPage(List<String> tags, int page, int size) {

        return new FeignPageImpl<>(Collections.emptyList(), 0, 0, 0);
    }

    @Override
    public CanvasDto.Response getCanvas(Long canvasId) {
        return null;
    }

    @Override
    public List<CanvasTagDto> getCanvasTags(boolean onlyUsed) {
        return Collections.emptyList();
    }

    @Override
    public ResponseEntity createCanvas(CanvasDto.Create dto) {
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity updateCanvas(Long id, CanvasDto.Update dto) {
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity deleteCanvas(Long id) {
        return ResponseEntity.badRequest().build();
    }

    @Override
    public Set<CanvasTagDto> getCanvasTags(Long id) {
        return Collections.emptySet();
    }

    @Override
    public ResponseEntity addCanvasTag(Long id, String title) {
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity removeCanvasTag(Long id, String title) {
        return ResponseEntity.badRequest().build();
    }

    @Override
    public FileUploadRespDto uploadFile(String category, FileUploadForm fileUploadForm) {
        return new FileUploadRespDto().setCode(-1).setMessage("failed to upload file");
    }

    @Override
    public ResponseEntity deleteFile(String category, String uuid) {
        return ResponseEntity.badRequest().build();
    }
}

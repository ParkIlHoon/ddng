package com.ddng.utilsapi.modules.canvas.repository;

import com.ddng.utilsapi.modules.canvas.domain.Canvas;
import com.ddng.utilsapi.modules.canvas.dto.CanvasDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface CanvasCustomRepository
{
    Page<Canvas> findCanvas(List<String> tags, Pageable pageable);
}

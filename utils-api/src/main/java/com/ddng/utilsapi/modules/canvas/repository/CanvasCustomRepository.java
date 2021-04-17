package com.ddng.utilsapi.modules.canvas.repository;

import com.ddng.utilsapi.modules.canvas.dto.CanvasDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CanvasCustomRepository
{
    Page<CanvasDto.Response> findCanvas(String[] tags, Pageable pageable);
}

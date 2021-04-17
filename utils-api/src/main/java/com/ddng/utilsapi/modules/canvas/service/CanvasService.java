package com.ddng.utilsapi.modules.canvas.service;

import com.ddng.utilsapi.modules.canvas.dto.CanvasDto;
import com.ddng.utilsapi.modules.canvas.repository.CanvasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CanvasService
{
    private final CanvasRepository canvasRepository;

    public Page<CanvasDto.Response> findCanvas(String[] tags, Pageable pageable) {
        return canvasRepository.findCanvas(tags, pageable);
    }
}

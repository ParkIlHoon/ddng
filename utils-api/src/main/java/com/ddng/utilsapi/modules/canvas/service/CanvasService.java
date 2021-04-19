package com.ddng.utilsapi.modules.canvas.service;

import com.ddng.utilsapi.modules.canvas.domain.Canvas;
import com.ddng.utilsapi.modules.canvas.domain.CanvasTag;
import com.ddng.utilsapi.modules.canvas.dto.CanvasDto;
import com.ddng.utilsapi.modules.canvas.dto.CanvasTagDto;
import com.ddng.utilsapi.modules.canvas.repository.CanvasRepository;
import com.ddng.utilsapi.modules.canvas.repository.CanvasTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CanvasService
{
    private final CanvasRepository canvasRepository;
    private final CanvasTagRepository canvasTagRepository;

    public Page<CanvasDto.Response> findCanvas(List<String> tags, Pageable pageable) {
        Page<Canvas> canvas = canvasRepository.findCanvas(tags, pageable);
        List<CanvasDto.Response> responseList = canvas.getContent().stream().map(CanvasDto.Response::new).collect(Collectors.toList());

        return new PageImpl<>(responseList, pageable, canvas.getTotalElements());
    }

    public Optional<Canvas> findCanvasById(Long id) {
        return canvasRepository.findById(id);
    }

    public List<CanvasTagDto> getAllCanvasTags() {
        return canvasTagRepository.findAll().stream().map(CanvasTagDto::new).collect(Collectors.toList());
    }

    public List<CanvasTagDto> getUsedCanvasTags() {
        return canvasTagRepository.findUsedTags().stream().map(CanvasTagDto::new).collect(Collectors.toList());
    }

    public Canvas createCanvas(CanvasDto.Create dto) {
        // 캔버스 생성
        Canvas newCanvas = new Canvas();
        newCanvas.setTitle(dto.getTitle());
        newCanvas.setFilePath(dto.getFilePath());
        newCanvas.setTopFixed(dto.isTopFixed());

        Canvas savedCanvas = canvasRepository.save(newCanvas);

        // 태그 존재 여부 확인
        Set<CanvasTagDto> dtoTags = dto.getTags();
        if (!dtoTags.isEmpty())
        {
            for (CanvasTagDto tagDto : dtoTags)
            {
                Optional<CanvasTag> firstByTitle = canvasTagRepository.findFirstByTitle(tagDto.getTitle());
                if (firstByTitle.isPresent())
                {
                    savedCanvas.getTags().add(firstByTitle.get());
                }
                else
                {
                    CanvasTag newTag = new CanvasTag();
                    newTag.setTitle(tagDto.getTitle());
                    CanvasTag savedTag = canvasTagRepository.save(newTag);
                    savedCanvas.getTags().add(savedTag);
                }
            }
        }

        return savedCanvas;
    }

    public void deleteCanvas(Canvas canvas) {
        canvasRepository.delete(canvas);
    }

    public void addTag(Canvas canvas, String title)
    {
        Optional<CanvasTag> firstByTitle = canvasTagRepository.findFirstByTitle(title);
        if (firstByTitle.isPresent())
        {
            canvas.getTags().add(firstByTitle.get());
        }
        else
        {
            CanvasTag newTag = new CanvasTag();
            newTag.setTitle(title);
            CanvasTag savedTag = canvasTagRepository.save(newTag);
            canvas.getTags().add(savedTag);
        }
    }

    public void removeTag(Canvas canvas, String title)
    {
        Optional<CanvasTag> firstByTitle = canvasTagRepository.findFirstByTitle(title);
        if (firstByTitle.isPresent())
        {
            canvas.getTags().remove(firstByTitle.get());
        }
    }

    public Canvas updateCanvas(Canvas canvas, CanvasDto.Update dto)
    {
        canvas.setTitle(dto.getTitle());
        canvas.setFilePath(dto.getFilePath());
        canvas.setTopFixed(dto.isTopFixed());

        return canvasRepository.save(canvas);
    }
}

package com.ddng.utilsapi.modules.canvas.repository;

import com.ddng.utilsapi.modules.canvas.domain.Canvas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CanvasRepository extends JpaRepository<Canvas, Long>, CanvasCustomRepository
{
}

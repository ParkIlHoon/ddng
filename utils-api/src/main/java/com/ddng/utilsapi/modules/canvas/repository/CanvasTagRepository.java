package com.ddng.utilsapi.modules.canvas.repository;

import com.ddng.utilsapi.modules.canvas.domain.CanvasTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface CanvasTagRepository extends JpaRepository<CanvasTag, Long>, CanvasTagCustomRepository
{
    Optional<CanvasTag> findFirstByTitle(String title);
}

package com.ddng.utilsapi.modules.canvas.repository;

import com.ddng.utilsapi.modules.canvas.domain.CanvasTag;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface CanvasTagCustomRepository {
    List<CanvasTag> findUsedTags();
}

package com.ddng.utilsapi.modules.canvas.repository;

import com.ddng.utilsapi.modules.canvas.domain.CanvasTag;
import com.ddng.utilsapi.modules.canvas.domain.QCanvas;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.ddng.utilsapi.modules.canvas.domain.QCanvas.canvas;
import static com.ddng.utilsapi.modules.canvas.domain.QCanvasTag.canvasTag;

public class CanvasTagRepositoryImpl implements CanvasTagCustomRepository
{
    private JPAQueryFactory queryFactory;

    public CanvasTagRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<CanvasTag> findUsedTags() {
        return queryFactory.select(canvasTag)
                .from(canvas)
                .join(canvas.tags, canvasTag)
                .distinct()
                .fetch();
    }
}

package com.ddng.utilsapi.modules.canvas.repository;

import com.ddng.utilsapi.modules.canvas.dto.CanvasDto;
import com.ddng.utilsapi.modules.canvas.dto.QCanvasDto_Response;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import static com.ddng.utilsapi.modules.canvas.domain.QCanvas.canvas;
import static com.ddng.utilsapi.modules.canvas.domain.QCanvasTag.canvasTag;

public class CanvasRepositoryImpl implements CanvasCustomRepository
{
    private final JPAQueryFactory queryFactory;

    public CanvasRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public Page<CanvasDto.Response> findCanvas(String[] tags, Pageable pageable)
    {
        QueryResults<CanvasDto.Response> queryResults =
                queryFactory.select(new QCanvasDto_Response(canvas.id, canvas.title, canvas.isTopFixed, canvas.createDate, canvas.tags))
                        .from(canvas)
                        .join(canvas.tags, canvasTag)
                        .where(canvasTag.title.in(tags))
                        .groupBy(canvas.id)
                        .orderBy(canvas.isTopFixed.desc(), canvas.createDate.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetchResults();

        return new PageImpl<>(queryResults.getResults(), pageable, queryResults.getTotal());
    }
}

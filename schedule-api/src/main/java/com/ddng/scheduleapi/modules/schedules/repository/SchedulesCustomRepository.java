package com.ddng.scheduleapi.modules.schedules.repository;

import com.ddng.scheduleapi.modules.schedules.domain.Schedules;
import com.ddng.scheduleapi.modules.schedules.dto.SchedulesDto;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <h1>스케쥴 커스텀 리포지토리 클래스</h1>
 */
@Transactional(readOnly = true)
public interface SchedulesCustomRepository
{
    /**
     * 특정 기간 내 스케쥴 목록을 조회한다.
     * @param startDate 시작일시
     * @param endDate 종료일시
     * @return 시작일시와 종료일시 사이에 포함되는 스케쥴 목록
     */
    List<Schedules> getSchedulesForDuration(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 스케쥴 목록을 조회한다.
     * @param dto 조회 조건
     * @return
     */
    List<Schedules> searchSchedules(SchedulesDto.Get dto);

    /**
     * 특정 일자의 스케쥴 목록을 조회한다.
     * @param dto 조회 조건
     * @return
     */
    List<Schedules> getCertainDaysSchedules(SchedulesDto.Get dto);
}
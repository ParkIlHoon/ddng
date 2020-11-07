package com.ddng.scheduleapi.modules.schedules.repository;

import com.ddng.scheduleapi.modules.schedules.domain.Schedules;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional(readOnly = true)
public interface SchedulesRepository extends JpaRepository<Schedules, Long>
{
    Page<Schedules> findByStartDateAfterAndEndDateBefore(LocalDateTime startDate, LocalDateTime endDate);
}

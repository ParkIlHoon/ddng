package com.ddng.scheduleapi.modules.schedules.repository;

import com.ddng.scheduleapi.modules.schedules.domain.Schedules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface SchedulesRepository extends JpaRepository<Schedules, Long>
{
    List<Schedules> findByStartDateGreaterThanEqualAndEndDateLessThanEqual(LocalDateTime startDate, LocalDateTime endDate);

    List<Schedules> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDateTime startDateTime, LocalDateTime endDateTime);
}

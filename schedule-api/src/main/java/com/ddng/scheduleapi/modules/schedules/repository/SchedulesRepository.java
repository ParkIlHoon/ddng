package com.ddng.scheduleapi.modules.schedules.repository;

import com.ddng.scheduleapi.modules.schedules.domain.Schedules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface SchedulesRepository extends JpaRepository<Schedules, Long>
{
}

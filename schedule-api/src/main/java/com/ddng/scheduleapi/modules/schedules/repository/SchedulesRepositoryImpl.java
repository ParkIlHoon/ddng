package com.ddng.scheduleapi.modules.schedules.repository;

import com.ddng.scheduleapi.modules.schedules.domain.Schedules;
import com.querydsl.core.QueryResults;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDateTime;
import java.util.List;

import static com.ddng.scheduleapi.modules.schedules.domain.QSchedules.schedules;

public class SchedulesRepositoryImpl extends QuerydslRepositorySupport implements SchedulesCustomRepository
{

    public SchedulesRepositoryImpl()
    {
        super(Schedules.class);
    }

    @Override
    public List<Schedules> getSchedulesForDuration(LocalDateTime startDate, LocalDateTime endDate)
    {
        QueryResults<Schedules> schedulesQueryResults = from(schedules)
                .where(
                        schedules.startDate.after(startDate)
                                .and(schedules.endDate.before(endDate))
                                .or(schedules.startDate.before(startDate)
                                        .or(schedules.endDate.after(endDate)))
                ).fetchResults();

        return schedulesQueryResults.getResults();
    }
}

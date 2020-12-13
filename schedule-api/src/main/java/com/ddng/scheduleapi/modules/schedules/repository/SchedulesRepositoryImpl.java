package com.ddng.scheduleapi.modules.schedules.repository;

import com.ddng.scheduleapi.modules.schedules.domain.ScheduleType;
import com.ddng.scheduleapi.modules.schedules.domain.Schedules;
import com.ddng.scheduleapi.modules.schedules.dto.SchedulesDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.ddng.scheduleapi.modules.schedules.domain.QSchedules.schedules;
import static org.springframework.util.StringUtils.hasText;

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

    @Override
    public List<Schedules> searchSchedules(SchedulesDto.Get dto)
    {
        QueryResults<Schedules> queryResults = from(schedules)
                .where(
                        nameEq(dto.getName()),
                        scheduleTypeEq(dto.getScheduleType()),
                        scheduleDuration(dto.getStartDate(), dto.getEndDate()),
                        allDayEq(dto.getAllDay()),
                        customerIdEq(dto.getCustomerId()),
                        userIdEq(dto.getUserId()),
                        bigoLike(dto.getBigo()),
                        payedEq(dto.getPayed())
                      )
                .fetchResults();

        return queryResults.getResults();
    }

    @Override
    public List<Schedules> getCertainDaysSchedules(SchedulesDto.Get dto)
    {
        LocalDateTime start = LocalDate.parse(dto.getBaseDate()).atTime(LocalTime.MAX);
        LocalDateTime end = LocalDate.parse(dto.getBaseDate()).atTime(LocalTime.MIN);

        QueryResults<Schedules> queryResults = from(schedules)
                .where(
                        schedules.startDate.loe(start)
                        .and(schedules.endDate.goe(end)),
                        nameEq(dto.getName()),
                        scheduleTypeEq(dto.getScheduleType()),
                        scheduleDuration(dto.getStartDate(), dto.getEndDate()),
                        allDayEq(dto.getAllDay()),
                        customerIdEq(dto.getCustomerId()),
                        userIdEq(dto.getUserId()),
                        bigoLike(dto.getBigo()),
                        payedEq(dto.getPayed())
                )
                .fetchResults();

        return queryResults.getResults();
    }

    private BooleanExpression nameEq(String name)
    {
        return hasText(name)? schedules.name.eq(name) : null;
    }
    private BooleanExpression scheduleTypeEq(ScheduleType type)
    {
        return type != null? schedules.type.eq(type) : null;
    }
    private BooleanExpression scheduleDuration(String startDate, String endDate)
    {
        if(hasText(startDate) && hasText(endDate))
        {
            LocalDateTime startDateTime = LocalDate.parse(startDate).atTime(LocalTime.MIN);
            LocalDateTime endDateTime = LocalDate.parse(endDate).atTime(LocalTime.MAX);
            return (schedules.startDate.after(startDateTime)
                    .and(schedules.endDate.before(endDateTime))
                    .or(schedules.startDate.before(startDateTime)
                            .or(schedules.endDate.after(endDateTime))));
        }
        else
        {
            return null;
        }
    }
    private BooleanExpression allDayEq(Boolean allDay)
    {
        return allDay != null? schedules.allDay.eq(allDay) : null;
    }
    private BooleanExpression customerIdEq(Long customerId)
    {
        return customerId != null? schedules.customerId.eq(customerId) : null;
    }
    private BooleanExpression userIdEq(Long userId)
    {
        return userId != null? schedules.userId.eq(userId) : null;
    }
    private BooleanExpression bigoLike(String bigo)
    {
        return hasText(bigo)? schedules.bigo.likeIgnoreCase(bigo) : null;
    }
    private BooleanExpression payedEq(Boolean payed)
    {
        return payed != null? schedules.payed.eq(payed) : null;
    }
}

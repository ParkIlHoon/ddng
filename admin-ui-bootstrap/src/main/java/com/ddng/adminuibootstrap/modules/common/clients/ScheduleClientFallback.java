package com.ddng.adminuibootstrap.modules.common.clients;

import com.ddng.adminuibootstrap.modules.common.dto.schedule.ScheduleDto;
import com.ddng.adminuibootstrap.modules.common.dto.schedule.ScheduleTagDto;
import com.ddng.adminuibootstrap.modules.common.dto.schedule.ScheduleTypeDto;
import com.ddng.adminuibootstrap.modules.schedules.form.ScheduleForm;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduleClientFallback implements ScheduleClient{

    @Override
    public ScheduleDto getSchedule(Long scheduleId) {
        return null;
    }

    @Override
    public List<ScheduleDto> getSchedules(String startDate, String endDate) {
        return Collections.emptyList();
    }

    @Override
    public List<ScheduleDto> getCertainDaySchedule(String baseDate, boolean payed) {
        return Collections.emptyList();
    }

    @Override
    public List<ScheduleDto> getCertainDaySchedule(String baseDate) {
        return Collections.emptyList();
    }

    @Override
    public List<ScheduleTypeDto> getScheduleTypes() {
        return Collections.emptyList();
    }

    @Override
    public List<ScheduleTagDto> getScheduleTags() {
        return Collections.emptyList();
    }

    @Override
    public ResponseEntity<String> createSchedule(ScheduleForm scheduleForm) {
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity updateSchedule(Long scheduleId, ScheduleForm scheduleForm) {
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity deleteSchedule(Long scheduleId) {
        return ResponseEntity.badRequest().build();
    }
}

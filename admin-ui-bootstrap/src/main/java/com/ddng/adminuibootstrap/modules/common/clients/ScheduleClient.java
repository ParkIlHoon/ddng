package com.ddng.adminuibootstrap.modules.common.clients;

import com.ddng.adminuibootstrap.modules.common.dto.schedule.ScheduleDto;
import com.ddng.adminuibootstrap.modules.common.dto.schedule.ScheduleTagDto;
import com.ddng.adminuibootstrap.modules.common.dto.schedule.ScheduleTypeDto;
import com.ddng.adminuibootstrap.modules.schedules.form.ScheduleForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <h1>ScheduleClient api Feign Client</h1>
 *
 * Spring Cloud Feign 기능을 사용해 RestTemplate 코드를 작성하지 않고 요청 기능을 구현한다.
 */
@FeignClient(name = "ddng-schedule-api")
public interface ScheduleClient
{
    String SCHEDULE_API = "/schedules";
    String TAG_API = "/tags";

    /**
     * 스케쥴을 조회하는 API를 호출한다.
     * @param scheduleId 조회할 스케쥴 아이디
     * @return 아이디에 해당하는 스케쥴
     */
    @GetMapping(SCHEDULE_API + "/{scheduleId}")
    ScheduleDto getSchedule(@PathVariable("scheduleId") Long scheduleId);

    /**
     * 기간 내에 해당하는 스케쥴 목록을 조회하는 API를 호출한다.
     * @param startDate 조회 기간 시작일자
     * @param endDate 조회 기간 종료일자
     * @return 조회 기간에 해당하는 스케쥴 목록
     */
    @GetMapping(SCHEDULE_API)
    List<ScheduleDto> getSchedules(@RequestParam("startDate") String startDate,
                                   @RequestParam("endDate") String endDate);

    /**
     * 특정 일자의 스케쥴 목록을 조회하는 API를 호출한다.
     * @param baseDate 조회 일자
     * @param payed 결재 여부
     * @return 조회 일자에 해당하는 스케쥴 목록
     */
    @GetMapping(SCHEDULE_API + "/day")
    List<ScheduleDto> getCertainDaySchedule(@RequestParam("baseDate") String baseDate,
                                            boolean payed);

    /**
     * 전체 스케쥴 타입 목록을 조회하는 API를 호출한다.
     * @return 전체 스케쥴 타입 목록
     */
    @GetMapping(SCHEDULE_API + "/types")
    List<ScheduleTypeDto> getScheduleTypes();

    /**
     * 전체 스케쥴 태그 목록을 조회하는 API를 호출한다.
     * @return 전체 스케쥴 태그 목록
     */
    @GetMapping(TAG_API)
    List<ScheduleTagDto> getScheduleTags();

    /**
     * 새로운 스케쥴을 생성하는 API를 호출한다.
     * @param scheduleForm 새로 생성하는 스케쥴 정보
     * @return 상태 코드
     */
    @PostMapping(SCHEDULE_API)
    String createSchedule(ScheduleForm scheduleForm);

    /**
     * 스케쥴을 수정하는 API를 호출한다.
     * @param scheduleId 수정할 스케쥴 아아디
     * @param scheduleForm 수정할 스케쥴 내용
     */
    @PutMapping(SCHEDULE_API + "/{scheduleId}")
    void updateSchedule(@PathVariable("scheduleId") Long scheduleId,
                        ScheduleForm scheduleForm);

    /**
     * 스케쥴을 삭제하는 API를 호출한다.
     * @param scheduleId 삭제할 스케쥴 아이디
     */
    @DeleteMapping(SCHEDULE_API + "/{scheduleId}")
    void deleteSchedule(@PathVariable("scheduleId") Long scheduleId);
}

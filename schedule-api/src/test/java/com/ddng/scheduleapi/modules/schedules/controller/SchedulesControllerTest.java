package com.ddng.scheduleapi.modules.schedules.controller;

import com.ddng.scheduleapi.modules.schedules.domain.CalendarType;
import com.ddng.scheduleapi.modules.schedules.domain.ScheduleType;
import com.ddng.scheduleapi.modules.schedules.domain.Schedules;
import com.ddng.scheduleapi.modules.schedules.dto.SchedulesDto;
import com.ddng.scheduleapi.modules.schedules.repository.SchedulesRepository;
import com.ddng.scheduleapi.modules.tag.domain.Tag;
import com.ddng.scheduleapi.modules.tag.dto.TagDto;
import com.ddng.scheduleapi.modules.tag.repository.TagRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
class SchedulesControllerTest
{
    @Autowired private WebApplicationContext ctx;
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private SchedulesRepository schedulesRepository;
    @Autowired private TagRepository tagRepository;

    @BeforeEach
    void initialize ()
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .build();

        Schedules today = new Schedules();
        today.setName("오늘");
        today.setStartDate(LocalDateTime.now());
        today.setEndDate(LocalDateTime.now());
        today.setAllDay(true);
        today.setType(ScheduleType.BEAUTY);

        schedulesRepository.save(today);

        Schedules week = new Schedules();
        week.setName("1주");
        week.setStartDate(LocalDate.now().plusWeeks(1).atStartOfDay());
        week.setEndDate(LocalDate.now().plusWeeks(1).atStartOfDay());
        week.setAllDay(true);
        week.setType(ScheduleType.HOTEL);

        schedulesRepository.save(week);

        Schedules week2 = new Schedules();
        week2.setName("2주");
        week2.setStartDate(LocalDate.now().plusWeeks(2).atStartOfDay());
        week2.setEndDate(LocalDate.now().plusWeeks(2).atStartOfDay());
        week2.setAllDay(true);
        week2.setType(ScheduleType.KINDERGARTEN);

        schedulesRepository.save(week2);

        Schedules week3 = new Schedules();
        week3.setName("3주");
        week3.setStartDate(LocalDate.now().plusWeeks(3).atStartOfDay());
        week3.setEndDate(LocalDate.now().plusWeeks(3).atStartOfDay());
        week3.setAllDay(true);
        week3.setType(ScheduleType.ETC);

        schedulesRepository.save(week3);

        Schedules month = new Schedules();
        month.setName("달");
        month.setStartDate(LocalDate.now().plusMonths(1).atStartOfDay());
        month.setEndDate(LocalDate.now().plusMonths(1).atStartOfDay());
        month.setAllDay(true);
        month.setType(ScheduleType.VACATION);

        schedulesRepository.save(month);
    }

    @AfterEach
    void reset ()
    {
        schedulesRepository.deleteAll();
        tagRepository.deleteAll();
    }

    @Test
    @DisplayName("스케쥴 유형 조회")
    void getSchedulesType() throws Exception
    {
        // given
        // when
        ResultActions actions = mockMvc.perform(get("/schedules/types"))
                                        .andDo(print())
                                        .andExpect(status().isOk());

        String contentAsString = actions.andReturn().getResponse().getContentAsString();
        JacksonJsonParser parser = new JacksonJsonParser();
        List<Object> parseList = parser.parseList(contentAsString);

        // then
        assertThat(parseList.size()).isEqualTo(ScheduleType.values().length);
        assertThat(((HashMap) parseList.get(0)).get("color")).isEqualTo(ScheduleType.BEAUTY.getColor());
    }

    @Test
    @DisplayName("스케쥴 조회 - 파라미터 누락")
    void getSchedules_missingParam() throws Exception
    {
        // given
        CalendarType calendarType = CalendarType.DAILY;
        String baseDate = LocalDate.now().toString();

        // when
        mockMvc.perform(
                        get("/schedules")
                            .param("baseDate", baseDate)
//                          .param("calendarType", calendarType.toString())
                        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("스케쥴 조회 - 오늘")
    void getSchedules_daily() throws Exception
    {
        // given
        CalendarType calendarType = CalendarType.DAILY;
        String baseDate = LocalDate.now().toString();

        // when
        ResultActions actions = mockMvc.perform(
                                                get("/schedules")
                                                        .param("baseDate", baseDate)
                                                        .param("calendarType", calendarType.toString())
                                                )
                                        .andDo(print())
                                        .andExpect(status().isOk());

        String contentAsString = actions.andReturn().getResponse().getContentAsString();
        JacksonJsonParser parser = new JacksonJsonParser();
        List<Object> parseList = parser.parseList(contentAsString);

        // then
        assertThat(parseList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("스케쥴 조회 - 주")
    void getSchedules_weekly() throws Exception
    {
        // given
        CalendarType calendarType = CalendarType.WEEKLY;
        String baseDate = LocalDate.now().toString();

        // when
        ResultActions actions = mockMvc.perform(
                get("/schedules")
                        .param("baseDate", baseDate)
                        .param("calendarType", calendarType.toString())
        )
                .andDo(print())
                .andExpect(status().isOk());

        String contentAsString = actions.andReturn().getResponse().getContentAsString();
        JacksonJsonParser parser = new JacksonJsonParser();
        List<Object> parseList = parser.parseList(contentAsString);

        // then
        assertThat(parseList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("스케쥴 조회 - 월")
    void getSchedules_monthly() throws Exception
    {
        // given
        CalendarType calendarType = CalendarType.MONTHLY;
        String baseDate = LocalDate.now().toString();

        // when
        ResultActions actions = mockMvc.perform(
                get("/schedules")
                        .param("baseDate", baseDate)
                        .param("calendarType", calendarType.toString())
        )
                .andDo(print())
                .andExpect(status().isOk());

        String contentAsString = actions.andReturn().getResponse().getContentAsString();
        JacksonJsonParser parser = new JacksonJsonParser();
        List<Object> parseList = parser.parseList(contentAsString);

        // then
        assertThat(parseList.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("스케쥴 조회 - 2주")
    void getSchedules_2week() throws Exception
    {
        // given
        CalendarType calendarType = CalendarType.TWO_WEEKS;
        String baseDate = LocalDate.now().toString();

        // when
        ResultActions actions = mockMvc.perform(
                get("/schedules")
                        .param("baseDate", baseDate)
                        .param("calendarType", calendarType.toString())
        )
                .andDo(print())
                .andExpect(status().isOk());

        String contentAsString = actions.andReturn().getResponse().getContentAsString();
        JacksonJsonParser parser = new JacksonJsonParser();
        List<Object> parseList = parser.parseList(contentAsString);

        // then
        assertThat(parseList.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("스케쥴 조회 - 3주")
    void getSchedules_3week() throws Exception
    {
        // given
        CalendarType calendarType = CalendarType.THREE_WEEKS;
        String baseDate = LocalDate.now().toString();

        // when
        ResultActions actions = mockMvc.perform(
                get("/schedules")
                        .param("baseDate", baseDate)
                        .param("calendarType", calendarType.toString())
        )
                .andDo(print())
                .andExpect(status().isOk());

        String contentAsString = actions.andReturn().getResponse().getContentAsString();
        JacksonJsonParser parser = new JacksonJsonParser();
        List<Object> parseList = parser.parseList(contentAsString);

        // then
        assertThat(parseList.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("스케쥴 생성")
    void createSchedule() throws Exception
    {
        // given
        SchedulesDto.Post dto = new SchedulesDto.Post();
        dto.setName("테스트 일정");
        dto.setStartDate(LocalDateTime.now().toString());
        dto.setEndDate(LocalDateTime.now().plusDays(1).toString());
        dto.setAllDay(false);
        dto.setScheduleType(ScheduleType.HOTEL);

        // when
        mockMvc.perform(
                        post("/schedules")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                        )
                .andDo(print())
                .andExpect(status().isCreated());

        // then
        List<Schedules> all = schedulesRepository.findAll();
        assertThat(all.size()).isGreaterThan(5);
    }

    @Test
    @DisplayName("스케쥴 수정")
    void updateSchedule() throws Exception
    {
        // given
        Schedules schedules = schedulesRepository.findAll().get(0);
        String beforeName = schedules.getName();
        String changeName = "변경된 스케쥴";
        LocalDateTime changeEndDate = LocalDateTime.now().plusDays(2);

        SchedulesDto.Put dto = new SchedulesDto.Put();
        dto.setName(changeName);
        dto.setAllDay(schedules.isAllDay());
        dto.setScheduleType(schedules.getType());
        dto.setStartDate(schedules.getStartDate().toString());
        dto.setEndDate(changeEndDate.toString());
        dto.setBigo(schedules.getBigo());
        dto.setUserId(schedules.getUserId());
        dto.setCustomerId(schedules.getCustomerId());

        // when
        mockMvc.perform(
                        put("/schedules/{id}", schedules.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                        )
                .andDo(print())
                .andExpect(status().isOk());

        // then
        Optional<Schedules> optional = schedulesRepository.findById(schedules.getId());
        Schedules updated = optional.get();

        assertThat(updated.getName()).isNotEqualTo(beforeName);
        assertThat(updated.getName()).isEqualTo(changeName);
        assertThat(updated.getEndDate()).isEqualTo(changeEndDate);
    }

    @Test
    @DisplayName("스케쥴 제거")
    void deleteSchedule() throws Exception
    {
        // given
        Schedules schedules = schedulesRepository.findAll().get(0);

        // when
        mockMvc.perform(delete("/schedules/{id}", schedules.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        // then
        Optional<Schedules> optional = schedulesRepository.findById(schedules.getId());

        assertThat(optional).isEmpty();
    }

    @Test
    @DisplayName("태그 추가")
    void addTag() throws Exception
    {
        // given
        Schedules schedules = schedulesRepository.findAll().get(0);
        TagDto dto = TagDto.builder().title("태그").build();

        // when
        mockMvc.perform(
                        post("/schedules/{id}/tags", schedules.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                        )
                .andDo(print())
                .andExpect(status().isOk());

        // then
        assertThat(schedules.getTags().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("태그 제거")
    void removeTag() throws Exception
    {
        // given
        Tag tag = new Tag();
        tag.setTitle("태그");
        tagRepository.save(tag);

        Schedules schedules = schedulesRepository.findAll().get(0);
        schedules.getTags().add(tag);

        schedulesRepository.save(schedules);
        TagDto dto = TagDto.builder().title("태그").build();

        // when
        mockMvc.perform(
                        delete("/schedules/{id}/tags", schedules.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                        )
                .andDo(print())
                .andExpect(status().isOk());

        // then
        assertThat(schedules.getTags().size()).isEqualTo(0);
    }
}
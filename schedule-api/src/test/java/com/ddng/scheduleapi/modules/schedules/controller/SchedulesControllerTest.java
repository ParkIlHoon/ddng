package com.ddng.scheduleapi.modules.schedules.controller;

import com.ddng.scheduleapi.modules.schedules.domain.ScheduleType;
import com.ddng.scheduleapi.modules.schedules.repository.SchedulesRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.HashMap;
import java.util.List;

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

    @BeforeEach
    void initialize ()
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .build();
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
}
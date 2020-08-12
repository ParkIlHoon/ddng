package com.ddng.userapi.user;

import com.ddng.userapi.common.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest extends BaseControllerTest
{

    @Test
    void 사용자_생성() throws Exception
    {
        UserDto dto = UserDto.builder()
                                .username("1hoon")
                                .password("1234")
                                .email("chiwoo2074@gmail.com")
                            .build();

        mockMvc.perform(
                        post("/users/")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaTypes.HAL_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(dto))
                        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                // HATEOAS 처리
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-users").exists())
                .andExpect(jsonPath("_links.update-user").exists())
                .andExpect(jsonPath("_links.delete-user").exists())
        ;
    }
}
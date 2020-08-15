package com.ddng.userapi.user;

import com.ddng.userapi.common.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class UserControllerTest extends BaseControllerTest
{
    @Autowired
    UserService userService;

    @Test
    void 사용자_생성() throws Exception
    {
        UserDto.Create dto = UserDto.Create.builder()
                                                .username("1hoon")
                                                .password("1234")
                                                .name("박일훈")
                                                .email("chiwoo2074@gmail.com")
                                                .telNo("010-1234-5678")
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
                // API Docs
                .andDo(
                        document("create-user",
                                links(
                                        halLinks(),
                                        linkWithRel("self").description("Link to Self"),
                                        linkWithRel("query-users").description("사용자 목록을 조회하는 링크"),
                                        linkWithRel("update-user").description("사용자를 수정하는 링크"),
                                        linkWithRel("delete-user").description("사용자를 삭제하는 링크"),
                                        linkWithRel("profile").description("Link to Profile")
                                ),
                                requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("헤더 ACCEPT 타입"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("헤더 CONTENT 타입")
                                ),
                                requestFields(
                                        fieldWithPath("username").description("생성할 사용자의 로그인 아이디"),
                                        fieldWithPath("password").description("생성할 사용자의 로그인 패스워드"),
                                        fieldWithPath("name").description("생성할 사용자의 이름"),
                                        fieldWithPath("email").description("생성할 사용자의 이메일 주소"),
                                        fieldWithPath("telNo").description("생성할 사용자의 전화번호")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("생성된 사용자의 고유 아이디"),
                                        fieldWithPath("username").description("생성된 사용자의 로그인 아이디"),
                                        fieldWithPath("password").description("생성된 사용자의 로그인 패스워드"),
                                        fieldWithPath("name").description("생성된 사용자의 이름"),
                                        fieldWithPath("email").description("생성된 사용자의 이메일 주소"),
                                        fieldWithPath("telNo").description("생성된 사용자의 전화번호"),
                                        fieldWithPath("joinDate").description("생성된 사용자의 가입일"),
                                        fieldWithPath("imagePath").description("생성된 사용자의 프로필 이미지 경로"),
                                        fieldWithPath("teamId").description("생성할 사용자의 소속 팀 고유 아이디"),
                                        fieldWithPath("grants").description("생성할 사용자의 권한부여 목록"),
                                        fieldWithPath("_links.self.href").description("생성된 사용자의 self link"),
                                        fieldWithPath("_links.query-users.href").description("query-users link"),
                                        fieldWithPath("_links.update-user.href").description("update-user link"),
                                        fieldWithPath("_links.delete-user.href").description("update-user link"),
                                        fieldWithPath("_links.profile.href").description("profile link")
                                )
                        )
                )
        ;
    }

    @Test
    void 사용자_조회() throws Exception
    {
        // given
        for (int idx = 0; idx < 10; idx++)
        {
            createUser(idx);
        }

        mockMvc.perform(
                        get("/users/")
                            .param("password", "1234")
                        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    private void createUser(int idx)
    {
        User user = User.builder()
                            .username("username_" + idx)
                            .password("1234")
                            .name("name_" + idx)
                            .joinDate(LocalDateTime.now())
                        .build();

        userService.save(user);
    }
}
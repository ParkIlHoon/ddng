package com.ddng.userapi.user;

import com.ddng.userapi.common.BaseControllerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class UserControllerTest extends BaseControllerTest
{
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserFactory userFactory;

    @AfterEach
    void resetDatas ()
    {
        userRepository.deleteAll();
    }

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
                                        fieldWithPath("teamId").description("생성된 사용자의 소속 팀 고유 아이디"),
                                        fieldWithPath("grants").description("생성된 사용자의 권한부여 목록"),
                                        fieldWithPath("_links.self.href").description("생성된 사용자의 self link"),
                                        fieldWithPath("_links.query-users.href").description("query-users link"),
                                        fieldWithPath("_links.update-user.href").description("update-user link"),
                                        fieldWithPath("_links.delete-user.href").description("delete-user link"),
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
            userFactory.createUser(idx);
        }

        mockMvc.perform(
                        get("/users/")
                            .param("password", "1234")
                        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].password").value("1234"))
        ;
    }

    @Test
    void 사용자_단일_조회() throws Exception
    {
        // given
        for (int idx = 0; idx < 10; idx++)
        {
            userFactory.createUser(idx);
        }
        User searchTarget = userFactory.createUser(10);

        mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/users/{id}", searchTarget.getId())
                        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(searchTarget.getId()))
                .andExpect(jsonPath("username").value(searchTarget.getUsername()))
                // HATEOAS 처리
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-users").exists())
                .andExpect(jsonPath("_links.update-user").exists())
                .andExpect(jsonPath("_links.delete-user").exists())
                // API Docs
                .andDo(
                        document("get-user",
                                links(
                                        linkWithRel("self").description("Link to Self"),
                                        linkWithRel("query-users").description("사용자 목록을 조회하는 링크"),
                                        linkWithRel("update-user").description("사용자를 수정하는 링크"),
                                        linkWithRel("delete-user").description("사용자를 삭제하는 링크"),
                                        linkWithRel("profile").description("Link to Profile")
                                ),
                                pathParameters(
                                        parameterWithName("id").description("조회할 사용자의 고유 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("조회된 사용자의 고유 아이디"),
                                        fieldWithPath("username").description("조회된 사용자의 로그인 아이디"),
                                        fieldWithPath("password").description("조회된 사용자의 로그인 패스워드"),
                                        fieldWithPath("name").description("조회된 사용자의 이름"),
                                        fieldWithPath("email").description("조회된 사용자의 이메일 주소"),
                                        fieldWithPath("telNo").description("조회된 사용자의 전화번호"),
                                        fieldWithPath("joinDate").description("조회된 사용자의 가입일"),
                                        fieldWithPath("imagePath").description("조회된 사용자의 프로필 이미지 경로"),
                                        fieldWithPath("teamId").description("조회된 사용자의 소속 팀 고유 아이디"),
                                        fieldWithPath("grants").description("조회된 사용자의 권한부여 목록"),
                                        fieldWithPath("_links.self.href").description("조회된 사용자의 self link"),
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
    void 사용자_수정() throws Exception
    {
        User createdUser = userFactory.createUser(1);

        UserDto.Update dto = UserDto.Update.builder()
                                                .username("1hoon")
                                                .password("5678")
                                                .email("chiwoo2074@gmail.com")
                                                .name("박일훈")
                                            .build();

        mockMvc.perform(
                        put("/users/" + createdUser.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaTypes.HAL_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(dto))
                        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("username").value(dto.getUsername()))
                .andExpect(jsonPath("password").value(dto.getPassword()))
                .andExpect(jsonPath("email").value(dto.getEmail()))
                .andExpect(jsonPath("name").value(dto.getName()))
                // HATEOAS 처리
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-users").exists())
                .andExpect(jsonPath("_links.update-user").exists())
                .andExpect(jsonPath("_links.delete-user").exists())
                // API Docs
//                .andDo(
//                        document("update-user",
//                                links(
//                                        halLinks(),
//                                        linkWithRel("self").description("Link to Self"),
//                                        linkWithRel("query-users").description("사용자 목록을 조회하는 링크"),
//                                        linkWithRel("update-user").description("사용자를 수정하는 링크"),
//                                        linkWithRel("delete-user").description("사용자를 삭제하는 링크"),
//                                        linkWithRel("profile").description("Link to Profile")
//                                ),
//                                requestHeaders(
//                                        headerWithName(HttpHeaders.ACCEPT).description("헤더 ACCEPT 타입"),
//                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("헤더 CONTENT 타입")
//                                ),
//                                requestFields(
//                                        fieldWithPath("username").description("생성할 사용자의 로그인 아이디"),
//                                        fieldWithPath("password").description("생성할 사용자의 로그인 패스워드"),
//                                        fieldWithPath("name").description("생성할 사용자의 이름"),
//                                        fieldWithPath("email").description("생성할 사용자의 이메일 주소"),
//                                        fieldWithPath("telNo").description("생성할 사용자의 전화번호")
//                                ),
//                                responseFields(
//                                        fieldWithPath("id").description("생성된 사용자의 고유 아이디"),
//                                        fieldWithPath("username").description("생성된 사용자의 로그인 아이디"),
//                                        fieldWithPath("password").description("생성된 사용자의 로그인 패스워드"),
//                                        fieldWithPath("name").description("생성된 사용자의 이름"),
//                                        fieldWithPath("email").description("생성된 사용자의 이메일 주소"),
//                                        fieldWithPath("telNo").description("생성된 사용자의 전화번호"),
//                                        fieldWithPath("joinDate").description("생성된 사용자의 가입일"),
//                                        fieldWithPath("imagePath").description("생성된 사용자의 프로필 이미지 경로"),
//                                        fieldWithPath("teamId").description("생성된 사용자의 소속 팀 고유 아이디"),
//                                        fieldWithPath("grants").description("생성된 사용자의 권한부여 목록"),
//                                        fieldWithPath("_links.self.href").description("생성된 사용자의 self link"),
//                                        fieldWithPath("_links.query-users.href").description("query-users link"),
//                                        fieldWithPath("_links.update-user.href").description("update-user link"),
//                                        fieldWithPath("_links.delete-user.href").description("update-user link"),
//                                        fieldWithPath("_links.profile.href").description("profile link")
//                                )
//                        )
//                )
        ;
    }

    @Test
    void 사용자_제거() throws Exception
    {
        User createdUser = userFactory.createUser(1);

        mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/users/{id}", createdUser.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaTypes.HAL_JSON_VALUE)
                        )
                .andDo(print())
                .andExpect(status().isOk())
                // API Docs
                .andDo(
                        document(
                                "delete-user",
                                pathParameters(
                                                parameterWithName("id").description("제거할 사용자의 아이디")
                                                )
                                )
                        )
        ;

        Optional<User> byId = userRepository.findById(createdUser.getId());
        assertTrue(byId.isEmpty());
    }

    @Test
    void 사용자_제거_존재하지않는사용자() throws Exception
    {
        mockMvc.perform(
                        delete("/users/{id}", 2L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaTypes.HAL_JSON_VALUE)
                        )
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
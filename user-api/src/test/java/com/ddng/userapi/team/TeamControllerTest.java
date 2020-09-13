package com.ddng.userapi.team;

import com.ddng.userapi.common.BaseControllerTest;
import com.ddng.userapi.user.User;
import com.ddng.userapi.user.UserFactory;
import com.ddng.userapi.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@SpringBootTest
class TeamControllerTest extends BaseControllerTest
{
    @Autowired
    private UserFactory userFactory;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamFactory teamFactory;
    @Autowired
    private TeamRepository teamRepository;

    private User manager;

    @BeforeEach
    void createUser ()
    {
        this.manager = userFactory.createUser(1);
    }

    @AfterEach
    void resetDatas ()
    {
        teamRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void 팀_생성() throws Exception
    {
        TeamDto.Create dto = TeamDto.Create.builder()
                                                .name("팀A")
                                                .path("teamA")
                                                .managerId(this.manager.getId())
                                            .build();

        mockMvc.perform(
                        post("/teams")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaTypes.HAL_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(dto))
                        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("managers[0]").value(this.manager.getId()))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                // HATEOAS 처리
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-teams").exists())
                .andExpect(jsonPath("_links.update-team").exists())
                .andExpect(jsonPath("_links.delete-team").exists())
                // API Docs
                .andDo(
                        document("create-team",
                                links(
                                        halLinks(),
                                        linkWithRel("self").description("Link to Self"),
                                        linkWithRel("query-teams").description("팀 목록을 조회하는 링크"),
                                        linkWithRel("update-team").description("팀을 수정하는 링크"),
                                        linkWithRel("delete-team").description("팀을 삭제하는 링크"),
                                        linkWithRel("profile").description("Link to Profile")
                                ),
                                requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("헤더 ACCEPT 타입"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("헤더 CONTENT 타입")
                                ),
                                requestFields(
                                        fieldWithPath("name").description("생성할 팀의 이름"),
                                        fieldWithPath("path").description("생성할 팀의 URI 경로"),
                                        fieldWithPath("managerId").description("생성할 팀의 매니저 사용자 아이디")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("생성된 팀의 고유 아이디"),
                                        fieldWithPath("name").description("생성된 팀의 이름"),
                                        fieldWithPath("path").description("생성된 팀의 URI 경로"),
                                        fieldWithPath("managers").description("생성된 팀의 매니저 사용자 아이디 목록"),
                                        fieldWithPath("members").description("생성된 팀의 멤버 사용자 아이디 목록"),
                                        fieldWithPath("_links.self.href").description("생성된 팀의 self link"),
                                        fieldWithPath("_links.query-teams.href").description("query-teams link"),
                                        fieldWithPath("_links.update-team.href").description("update-team link"),
                                        fieldWithPath("_links.delete-team.href").description("delete-team link"),
                                        fieldWithPath("_links.profile.href").description("profile link")
                                )
                        )
                )
        ;
    }
}
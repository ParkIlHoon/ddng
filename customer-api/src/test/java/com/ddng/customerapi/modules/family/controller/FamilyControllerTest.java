package com.ddng.customerapi.modules.family.controller;

import com.ddng.customerapi.modules.customer.CustomerFactory;
import com.ddng.customerapi.modules.customer.domain.Customer;
import com.ddng.customerapi.modules.customer.domain.CustomerType;
import com.ddng.customerapi.modules.family.domain.Family;
import com.ddng.customerapi.modules.family.repository.FamilyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
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
@AutoConfigureMockMvc
@Transactional
class FamilyControllerTest
{
    @Autowired private WebApplicationContext ctx;
    @Autowired private MockMvc mockMvc;
    @Autowired private CustomerFactory customerFactory;
    @Autowired private FamilyRepository familyRepository;

    @BeforeEach
    void initializeData ()
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .build();

        Customer chiwoo = customerFactory.createCustomerWithNameAndType("치우", CustomerType.MALTESE);
        Customer vini = customerFactory.createCustomerWithNameAndType("비니", CustomerType.YORKSHIRE_TERRIER);
        Customer bolt = customerFactory.createCustomerWithNameAndType("볼트", CustomerType.MALTESE);
        Customer rani = customerFactory.createCustomerWithNameAndType("라니", CustomerType.SHIH_TZU);

        Customer aga = customerFactory.createCustomerWithNameAndType("아가", CustomerType.MALTESE);
        Customer gomsun = customerFactory.createCustomerWithNameAndType("곰순", CustomerType.YORKSHIRE_TERRIER);

        Family family = new Family(chiwoo);
        familyRepository.save(family);
        family.addMembers(List.of(vini, bolt, rani));

        Family family2 = new Family(aga);
        familyRepository.save(family2);
        family2.addMember(gomsun);
    }

    @Test
    @DisplayName("가족 검색-가족 이름")
    void getFamilyList_familyName() throws Exception
    {
        // given
        String keyword = "치우(이)네 가족";

        // when
        ResultActions actions = mockMvc.perform(
                                                get("/family")
                                                    .param("keyword", keyword)
                                                    .param("page", "0")
                                                )
                                        .andDo(print())
                                        .andExpect(status().isOk());

        // then
        MockHttpServletResponse response = actions.andReturn().getResponse();
        String contentAsString = response.getContentAsString();
        JacksonJsonParser parser = new JacksonJsonParser();
        List<Object> objectList = parser.parseList(contentAsString);

        assertThat(objectList.size()).isEqualTo(1);
        assertThat(((HashMap) objectList.get(0)).get("name")).isEqualTo(keyword);
    }

    @Test
    @DisplayName("가족 검색-구성원 이름")
    void getFamilyList_memberName() throws Exception
    {
        // given
        String keyword = "라니";

        // when
        ResultActions actions = mockMvc.perform(
                                                get("/family")
                                                    .param("keyword", keyword)
                                                    .param("page", "0")
                                                )
                                        .andDo(print())
                                        .andExpect(status().isOk());

        // then
        MockHttpServletResponse response = actions.andReturn().getResponse();
        String contentAsString = response.getContentAsString();
        JacksonJsonParser parser = new JacksonJsonParser();
        List<Object> objectList = parser.parseList(contentAsString);

        assertThat(objectList.size()).isEqualTo(1);
        assertThat(((HashMap) objectList.get(0)).get("name")).isEqualTo("치우(이)네 가족");
    }

    @Test
    @DisplayName("가족 검색-구성원 전화번호")
    void getFamilyList_memberTelNo() throws Exception
    {
        // given
        String keyword = "01012345678";

        // when
        ResultActions actions = mockMvc.perform(
                                                get("/family")
                                                    .param("keyword", keyword)
                                                    .param("page", "0")
                                                )
                                        .andDo(print())
                                        .andExpect(status().isOk());

        // then
        MockHttpServletResponse response = actions.andReturn().getResponse();
        String contentAsString = response.getContentAsString();
        JacksonJsonParser parser = new JacksonJsonParser();
        List<Object> objectList = parser.parseList(contentAsString);

        assertThat(objectList.size()).isEqualTo(2);
        assertThat(((HashMap) objectList.get(0)).get("name")).isEqualTo("아가(이)네 가족");
        assertThat(((HashMap) objectList.get(1)).get("name")).isEqualTo("치우(이)네 가족");
    }

    @Test
    @DisplayName("가족 검색-구성원 품종")
    void getFamilyList_memberType() throws Exception
    {
        // given
        String keyword = CustomerType.MALTESE.getKorName();

        // when
        ResultActions actions = mockMvc.perform(
                                                get("/family")
                                                    .param("keyword", keyword)
                                                    .param("page", "0")
                                                )
                                        .andDo(print())
                                        .andExpect(status().isOk());

        // then
        MockHttpServletResponse response = actions.andReturn().getResponse();
        String contentAsString = response.getContentAsString();
        JacksonJsonParser parser = new JacksonJsonParser();
        List<Object> objectList = parser.parseList(contentAsString);

        assertThat(objectList.size()).isEqualTo(2);
        assertThat(((HashMap) objectList.get(0)).get("name")).isEqualTo("아가(이)네 가족");
        assertThat(((HashMap) objectList.get(1)).get("name")).isEqualTo("치우(이)네 가족");
    }

    @Test
    @DisplayName("가족 검색-구성원 품종2")
    void getFamilyList_memberType2() throws Exception
    {
        // given
        String keyword = CustomerType.SHIH_TZU.getKorName();

        // when
        ResultActions actions = mockMvc.perform(
                get("/family")
                        .param("keyword", keyword)
                        .param("page", "0")
        )
                .andDo(print())
                .andExpect(status().isOk());

        // then
        MockHttpServletResponse response = actions.andReturn().getResponse();
        String contentAsString = response.getContentAsString();
        JacksonJsonParser parser = new JacksonJsonParser();
        List<Object> objectList = parser.parseList(contentAsString);

        assertThat(objectList.size()).isEqualTo(1);
        assertThat(((HashMap) objectList.get(0)).get("name")).isEqualTo("치우(이)네 가족");
    }

}
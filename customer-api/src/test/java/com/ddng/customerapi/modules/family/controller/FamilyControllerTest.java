package com.ddng.customerapi.modules.family.controller;

import com.ddng.customerapi.modules.customer.CustomerFactory;
import com.ddng.customerapi.modules.customer.domain.Customer;
import com.ddng.customerapi.modules.customer.domain.CustomerType;
import com.ddng.customerapi.modules.customer.repository.CustomerRepository;
import com.ddng.customerapi.modules.family.domain.Family;
import com.ddng.customerapi.modules.family.dto.FamilyDto;
import com.ddng.customerapi.modules.family.repository.FamilyRepository;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class FamilyControllerTest
{
    @Autowired private WebApplicationContext ctx;
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private CustomerFactory customerFactory;
    @Autowired private FamilyRepository familyRepository;
    @Autowired private CustomerRepository customerRepository;

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

    @AfterEach
    void resetData ()
    {
        familyRepository.deleteAll();
    }

    @Test
    @DisplayName("가족 검색-가족 이름")
    void getFamilyList_familyName() throws Exception
    {
        // given
        String keyword = "치우(이)네 가족";

        // when
        ResultActions actions = mockMvc.perform(
                                                get("/families")
                                                    .param("keyword", keyword)
                                                    .param("page", "0")
                                                )
                                        .andDo(print())
                                        .andExpect(status().isOk());

        // then
        MockHttpServletResponse response = actions.andReturn().getResponse();
        String contentAsString = response.getContentAsString();
        JacksonJsonParser parser = new JacksonJsonParser();
        Map<String, Object> stringObjectMap = parser.parseMap(contentAsString);
        List objectList = (List) stringObjectMap.get("content");

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
                                                get("/families")
                                                    .param("keyword", keyword)
                                                    .param("page", "0")
                                                )
                                        .andDo(print())
                                        .andExpect(status().isOk());

        // then
        MockHttpServletResponse response = actions.andReturn().getResponse();
        String contentAsString = response.getContentAsString();
        JacksonJsonParser parser = new JacksonJsonParser();
        Map<String, Object> stringObjectMap = parser.parseMap(contentAsString);
        List objectList = (List) stringObjectMap.get("content");

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
                                                get("/families")
                                                    .param("keyword", keyword)
                                                    .param("page", "0")
                                                )
                                        .andDo(print())
                                        .andExpect(status().isOk());

        // then
        MockHttpServletResponse response = actions.andReturn().getResponse();
        String contentAsString = response.getContentAsString();
        JacksonJsonParser parser = new JacksonJsonParser();
        Map<String, Object> stringObjectMap = parser.parseMap(contentAsString);
        List objectList = (List) stringObjectMap.get("content");

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
                                                get("/families")
                                                    .param("keyword", keyword)
                                                    .param("page", "0")
                                                )
                                        .andDo(print())
                                        .andExpect(status().isOk());

        // then
        MockHttpServletResponse response = actions.andReturn().getResponse();
        String contentAsString = response.getContentAsString();
        JacksonJsonParser parser = new JacksonJsonParser();
        Map<String, Object> stringObjectMap = parser.parseMap(contentAsString);
        List objectList = (List) stringObjectMap.get("content");

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
                get("/families")
                        .param("keyword", keyword)
                        .param("page", "0")
        )
                .andDo(print())
                .andExpect(status().isOk());

        // then
        MockHttpServletResponse response = actions.andReturn().getResponse();
        String contentAsString = response.getContentAsString();
        JacksonJsonParser parser = new JacksonJsonParser();
        Map<String, Object> stringObjectMap = parser.parseMap(contentAsString);
        List objectList = (List) stringObjectMap.get("content");

        assertThat(objectList.size()).isEqualTo(1);
        assertThat(((HashMap) objectList.get(0)).get("name")).isEqualTo("치우(이)네 가족");
    }

    @Test
    @DisplayName("가족 생성")
    void createFamily() throws Exception
    {
        // given
        Customer customer = customerFactory.createCustomer();
        FamilyDto.Post dto = FamilyDto.Post.builder()
                                                .customerId(customer.getId())
                                            .build();

        // when
        mockMvc.perform(
                        post("/families")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                        )
                .andDo(print())
                .andExpect(status().isCreated());

        // then
        List<Family> all = familyRepository.findAll();

        assertThat(all.size()).isEqualTo(3);
        assertThat(all.get(2).getName()).isEqualTo(customer.getName() + "(이)네 가족");
        assertThat(all.get(2).getCustomers()).isNotEmpty();
        assertThat(all.get(2).getCustomers().get(0)).isEqualTo(customer);
    }

    @Test
    @DisplayName("가족 구성원 추가")
    void addMember() throws Exception
    {
        // given
        Customer customer = customerFactory.createCustomer();
        FamilyDto.Post dto = FamilyDto.Post.builder()
                                                .customerId(customer.getId())
                                            .build();

        Family family = familyRepository.findAll().get(1);

        // when
        mockMvc.perform(
                        post("/families/{id}/member", family.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                        )
                .andDo(print())
                .andExpect(status().isOk());

        // then
        assertThat(family.getCustomers()).contains(customer);
    }

    @Test
    @DisplayName("가족 구성원 제거")
    void removeMember() throws Exception
    {
        // given
        Family family = familyRepository.findAll().get(1);
        Customer customer = family.getCustomers().get(0);
        FamilyDto.Delete dto = FamilyDto.Delete.builder()
                .customerId(customer.getId())
                .build();

        // when
        mockMvc.perform(
                        delete("/families/{id}/member", family.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                        )
                .andDo(print())
                .andExpect(status().isOk());

        // then
        assertThat(customer).isNotIn(family);
        assertThat(family.getName()).isNotEqualTo(customer.getName() + "(이)네 가족");
    }

    @Test
    @DisplayName("가족 제거")
    void deleteFamily() throws Exception
    {
        // given
        Family family = familyRepository.findAll().get(0);
        Customer customer = family.getCustomers().get(0);

        // when
        mockMvc.perform(
                        delete("/families/{id}", family.getId())
                        )
                .andDo(print())
                .andExpect(status().isOk());

        // then
        Optional<Family> byId = familyRepository.findById(family.getId());
        assertThat(byId).isEmpty();
        assertThat(customer.getFamily()).isNull();
    }

    @Test
    @DisplayName("가족 수정")
    void updateFamily() throws Exception
    {
        // given
        Family family = familyRepository.findAll().get(0);
        String changeName = "변경한 이름";
        FamilyDto.Put dto = FamilyDto.Put.builder().name(changeName).build();

        // when
        mockMvc.perform(
                        put("/families/{id}", family.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                        )
                .andDo(print())
                .andExpect(status().isOk());

        // then
        Optional<Family> byId = familyRepository.findById(family.getId());
        assertThat(byId.get().getName()).isEqualTo(changeName);
    }
}
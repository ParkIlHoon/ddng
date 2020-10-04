package com.ddng.customerapi.modules.customer;

import com.ddng.customerapi.modules.customer.domain.Customer;
import com.ddng.customerapi.modules.customer.domain.CustomerType;
import com.ddng.customerapi.modules.customer.dto.CustomerDto;
import com.ddng.customerapi.modules.customer.repository.CustomerRepository;
import com.ddng.customerapi.modules.tag.domain.Tag;
import com.ddng.customerapi.modules.tag.dto.TagDto;
import com.ddng.customerapi.modules.tag.repository.TagRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class CustomerControllerTest
{
    @Autowired private WebApplicationContext ctx;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private TagRepository tagRepository;
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @BeforeEach
    void initializeData ()
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .build();

        // given
        Customer customer1 = Customer.builder()
                .name("customer1")
                .type(CustomerType.BEAGLE)
                .telNo("01040668366")
                .build();

        Customer customer2 = Customer.builder()
                .name("customer2")
                .type(CustomerType.MALTESE)
                .telNo("01040668366")
                .build();

        customerRepository.save(customer1);
        customerRepository.save(customer2);
    }

    @AfterEach
    void resetData ()
    {
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("고객 검색-이름 조건")
    void getCustomerList_name() throws Exception
    {
        // given
        String keyword = "customer";

        // when
        String contentAsString = getCustomer(keyword, 0);

        // then
        JacksonJsonParser parser = new JacksonJsonParser();
        List<Object> objects = parser.parseList(contentAsString);

        assertThat(objects.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("고객 검색-품종 조건")
    void getCustomerList_type() throws Exception
    {
        // given
        String keyword = CustomerType.MALTESE.getKorName();

        // when
        String contentAsString = getCustomer(keyword, 0);

        // then
        JacksonJsonParser parser = new JacksonJsonParser();
        List<Object> objects = parser.parseList(contentAsString);

        assertThat(objects.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("고객 검색-전화번호 조건")
    void getCustomerList_telNo() throws Exception
    {
        // given
        String keyword = "8366";

        // when
        String contentAsString = getCustomer(keyword, 0);

        // then
        JacksonJsonParser parser = new JacksonJsonParser();
        List<Object> objects = parser.parseList(contentAsString);

        assertThat(objects.size()).isEqualTo(2);
    }

    private String getCustomer(String keyword, int page) throws Exception
    {
        ResultActions actions = mockMvc.perform(
                                                get("/customer")
                                                        .param("keyword", keyword)
                                                        .param("page", String.valueOf(page))
                                                )
                                                .andDo(print())
                                                .andExpect(status().isOk());

        MockHttpServletResponse response = actions.andReturn().getResponse();
        return response.getContentAsString();
    }

    @Test
    @DisplayName("고객 검색-아이디 조건")
    void getCustomer() throws Exception
    {
        // given
        Customer customer = Customer.builder()
                                    .name("customer3")
                                    .type(CustomerType.YORKSHIRE_TERRIER)
                                    .telNo("01040668366")
                                    .build();

        Customer save = customerRepository.save(customer);

        // when
        ResultActions actions = mockMvc.perform(
                                                get("/customer/{id}", save.getId())
                                                )
                                        .andDo(print())
                                        .andExpect(status().isOk());

        MockHttpServletResponse response = actions.andReturn().getResponse();
        String contentAsString = response.getContentAsString();

        // then
        JacksonJsonParser parser = new JacksonJsonParser();
        Map<String, Object> stringObjectMap = parser.parseMap(contentAsString);

        assertThat(stringObjectMap.get("name")).isEqualTo(save.getName());
        assertThat(stringObjectMap.get("type")).isEqualTo(save.getType().toString());
    }

    @Test
    @DisplayName("고객 정보 수정")
    void updateCustomer() throws Exception
    {
        Customer customer = Customer.builder()
                .name("customer3")
                .type(CustomerType.YORKSHIRE_TERRIER)
                .telNo("01040668366")
                .build();

        Customer save = customerRepository.save(customer);

        CustomerDto.Put dto = CustomerDto.Put.builder()
                                                .name("치우")
                                                .type(CustomerType.MALTESE)
                                                .telNo("01012345678")
                                            .build();

        ResultActions actions = mockMvc.perform(
                                                put("/customer/{id}", save.getId())
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(objectMapper.writeValueAsString(dto))
                                                )
                                        .andDo(print())
                                        .andExpect(status().isOk());

        MockHttpServletResponse response = actions.andReturn().getResponse();
        String contentAsString = response.getContentAsString();

        // then
        JacksonJsonParser parser = new JacksonJsonParser();
        Map<String, Object> stringObjectMap = parser.parseMap(contentAsString);

        assertThat(stringObjectMap.get("name")).isEqualTo("치우");
        assertThat(stringObjectMap.get("type")).isEqualTo(CustomerType.MALTESE.toString());
        assertThat(stringObjectMap.get("telNo")).isEqualTo("01012345678");
    }

    @Test
    @DisplayName("고객 생성")
    void createCustomer() throws Exception
    {
        CustomerDto.Post dto = CustomerDto.Post.builder()
                                                    .name("치우")
                                                    .sexGb("MALE")
                                                    .telNo("01012345678")
                                                    .bigo("비고란")
                                                    .type(CustomerType.MALTESE)
                                                .build();

        ResultActions actions = mockMvc.perform(
                                                post("/customer")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(objectMapper.writeValueAsString(dto))
                                                )
                                        .andDo(print())
                                        .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("고객 삭제")
    void deleteCustomer() throws Exception
    {
        // given
        Customer customer3 = Customer.builder()
                                        .name("customer3")
                                        .type(CustomerType.YORKSHIRE_TERRIER)
                                        .telNo("01040668366")
                                    .build();

        Customer save = customerRepository.save(customer3);

        // when
        mockMvc.perform(
                        delete("/customer/{id}", save.getId())
                        )
                .andDo(print())
                .andExpect(status().isOk());

        // then
        List<Customer> all = customerRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
        assertThat(all.stream().filter(customer -> customer.getId().equals(save.getId())).count()).isEqualTo(0);
    }

    @Test
    @DisplayName("고객 태그-추가")
    void addCustomerTag() throws Exception
    {
        // given
        Customer customer3 = Customer.builder()
                                        .name("customer3")
                                        .type(CustomerType.YORKSHIRE_TERRIER)
                                        .telNo("01040668366")
                                        .tags(new HashSet<>())
                                    .build();
        Customer save = customerRepository.save(customer3);

        TagDto tagDto = new TagDto();
        tagDto.setTitle("입질 심함");

        // when
        mockMvc.perform(
                        post("/customer/{id}/tag", save.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(tagDto))
                        )
                .andDo(print())
                .andExpect(status().isOk());

        // then
        Optional<Customer> byId = customerRepository.findById(save.getId());
        assertThat(byId.get().getTags()).isNotEmpty();
        assertThat(byId.get().getTags().stream().filter(tag -> tag.getTitle().equals("입질 심함")).count()).isEqualTo(1);
    }

    @Test
    @DisplayName("고객 태그-제거")
    void removeCustomTag() throws Exception
    {
        // given
        Tag tag = tagRepository.save(Tag.builder().title("곱슬").build());

        Customer customer3 = Customer.builder()
                                    .name("customer3")
                                    .type(CustomerType.YORKSHIRE_TERRIER)
                                    .telNo("01040668366")
                                    .tags(new HashSet<>())
                                    .build();
        customer3.getTags().add(tag);
        Customer save = customerRepository.save(customer3);

        TagDto tagDto = new TagDto();
        tagDto.setTitle("곱슬");

        // when
        mockMvc.perform(
                        delete("/customer/{id}/tag", save.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tagDto))
                        )
                .andDo(print())
                .andExpect(status().isOk());

        // then
        Optional<Customer> byId = customerRepository.findById(save.getId());
        assertThat(byId.get().getTags()).isEmpty();
    }
}
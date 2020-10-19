package com.ddng.saleapi.modules.item.controller;

import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.item.domain.ItemType;
import com.ddng.saleapi.modules.item.dto.ItemDto;
import com.ddng.saleapi.modules.item.repository.ItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ItemControllerTest
{
    @Autowired private WebApplicationContext ctx;
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired ModelMapper modelMapper;
    @Autowired ItemRepository itemRepository;

    @BeforeEach
    public void initializeData ()
    {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .build();

        Item item = new Item();
        item.setName("상품1");
        item.setType(ItemType.SNACK);
        item.setBarcode("12345678");
        item.setPrice(1500);
        item.setItemQuantity(100);

        Item item2 = new Item();
        item2.setName("상품2");
        item2.setType(ItemType.FEED);
        item2.setBarcode("98765432");
        item2.setPrice(1500);
        item2.setItemQuantity(100);

        itemRepository.save(item);
        itemRepository.save(item2);
    }

    @AfterEach
    public void resetData ()
    {
        itemRepository.deleteAll();
    }

    @Test
    @DisplayName("상품 검색 - 이름 조건")
    void searchItem_byName() throws Exception
    {
        // given
        String keyword = "상품";

        // when
        List<HashMap> objectList = searchByKeyword(keyword);

        // then
        assertThat(objectList.size()).isEqualTo(2);
        assertThat(objectList.get(0).get("name")).isEqualTo("상품1");
        assertThat(objectList.get(1).get("name")).isEqualTo("상품2");
    }

    @Test
    @DisplayName("상품 검색 - 바코드 조건")
    void searchItem_byBarcode() throws Exception
    {
        // given
        String keyword = "12345678";

        // when
        List<HashMap> objectList = searchByKeyword(keyword);

        assertThat(objectList.size()).isEqualTo(1);
        assertThat(objectList.get(0).get("name")).isEqualTo("상품1");
        assertThat(objectList.get(0).get("barcode")).isEqualTo(keyword);
    }

    @Test
    @DisplayName("상품 검색 - 종류명 조건")
    void searchItem_byTypeName() throws Exception
    {
        // given
        String keyword = ItemType.FEED.getName();

        // when
        List<HashMap> objectList = searchByKeyword(keyword);

        assertThat(objectList.size()).isEqualTo(1);
        assertThat(objectList.get(0).get("name")).isEqualTo("상품2");
        assertThat(objectList.get(0).get("typeName")).isEqualTo(keyword);
    }

    private List<HashMap> searchByKeyword(String keyword) throws Exception
    {
        ResultActions actions = mockMvc.perform(
                                                get("/item")
                                                .param("keyword", keyword)
                                                .param("page", "0")
                                                )
                                        .andDo(print())
                                        .andExpect(status().isOk());

        MockHttpServletResponse response = actions.andReturn().getResponse();
        String contentAsString = response.getContentAsString();

        JacksonJsonParser parser = new JacksonJsonParser();
        Map<String, Object> stringObjectMap = parser.parseMap(contentAsString);
        return (List<HashMap>) stringObjectMap.get("content");
    }

    @Test
    @DisplayName("상품 조회 - 존재하는 상품")
    void getItem_exist() throws Exception
    {
        // given
        Item item = itemRepository.findAll().get(0);

        // when
        ResultActions actions = mockMvc.perform(
                                                get("/item/{id}", item.getId())
                                                )
                                        .andDo(print())
                                        .andExpect(status().isOk());

        MockHttpServletResponse response = actions.andReturn().getResponse();
        String contentAsString = response.getContentAsString();

        JacksonJsonParser parser = new JacksonJsonParser();
        Map<String, Object> stringObjectMap = parser.parseMap(contentAsString);

        // then
        assertThat(stringObjectMap.get("name")).isEqualTo("상품1");
    }

    @Test
    @DisplayName("상품 조회 - 존재하지않는 상품")
    void getItem_notExist() throws Exception
    {
        // given
        Long id = 99999L;

        // when
        mockMvc.perform(
                       get("/item/{id}", id)
                        )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("상품 생성")
    void createItem() throws Exception
    {
        // given
        ItemDto.Post dto = ItemDto.Post.builder()
                                        .name("상품3")
                                        .type(ItemType.SUPPLIES)
                                        .barcode("880208482113")
                                        .price(3500)
                                        .itemQuantity(10)
                                        .build();

        // when
        mockMvc.perform(
                        post("/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        )
                .andDo(print())
                .andExpect(status().isCreated());

        // then
        List<Item> all = itemRepository.findAll();
        assertThat(all.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("상품 생성 - 잘못된 값")
    void createItem_wrongValue() throws Exception
    {
        // given
        ItemDto.Post dto = ItemDto.Post.builder()
//                                        .name("상품3")
//                                        .type(ItemType.SUPPLIES)
                                        .barcode("880208482113")
                                        .price(3500)
                                        .itemQuantity(10)
                                        .build();

        // when
        mockMvc.perform(
                        post("/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        )
                .andDo(print())
                .andExpect(status().isBadRequest());

        // then
        List<Item> all = itemRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("상품 수정")
    void updateItem() throws Exception
    {
        // given
        String changeName = "변경된 이름";
        Item item = itemRepository.findAll().get(0);
        ItemDto.Put map = modelMapper.map(item, ItemDto.Put.class);
        map.setName(changeName);

        // when
        ResultActions actions = mockMvc.perform(
                                                put("/item/{id}", item.getId())
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .content(objectMapper.writeValueAsString(map))
                                                )
                                        .andDo(print())
                                        .andExpect(status().isOk());

        MockHttpServletResponse response = actions.andReturn().getResponse();
        String contentAsString = response.getContentAsString();

        JacksonJsonParser parser = new JacksonJsonParser();
        Map<String, Object> stringObjectMap = parser.parseMap(contentAsString);

        // then
        assertThat(stringObjectMap.get("name")).isEqualTo(changeName);
    }

    @Test
    @DisplayName("상품 수정 - 잘못된 값")
    void updateItem_wrongValue() throws Exception
    {
        // given
        String changeName = "변경된 이름";
        Item item = itemRepository.findAll().get(0);
        ItemDto.Put map = modelMapper.map(item, ItemDto.Put.class);
        map.setName(changeName);
        map.setType(null);

        // when
        ResultActions actions = mockMvc.perform(
                                                put("/item/{id}", item.getId())
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(map))
                                                )
                                        .andDo(print())
                                        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상품 삭제")
    void deleteItem_exist() throws Exception
    {
        // given
        Item item = itemRepository.findAll().get(0);

        // when
        mockMvc.perform(
                        delete("/item/{id}", item.getId())
                        )
                .andDo(print())
                .andExpect(status().isOk());

        // then
        List<Item> all = itemRepository.findAll();
        assertThat(item).isNotIn(all);
    }

    @Test
    @DisplayName("상품 삭제 - 존재하지않는 상품")
    void deleteItem_notExist() throws Exception
    {
        // given
        Long id = 9999L;

        // when
        mockMvc.perform(
                        delete("/item/{id}", id)
                        )
                .andDo(print())
                .andExpect(status().isBadRequest());

        // then
        List<Item> all = itemRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
    }
}
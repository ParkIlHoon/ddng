package com.ddng.saleapi.modules.item.controller;

import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.item.domain.ItemType;
import com.ddng.saleapi.modules.item.repository.ItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
}
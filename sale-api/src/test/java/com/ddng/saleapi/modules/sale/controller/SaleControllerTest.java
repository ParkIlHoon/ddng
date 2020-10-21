package com.ddng.saleapi.modules.sale.controller;

import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.item.domain.ItemType;
import com.ddng.saleapi.modules.item.repository.ItemRepository;
import com.ddng.saleapi.modules.sale.domain.PaymentType;
import com.ddng.saleapi.modules.sale.domain.Sale;
import com.ddng.saleapi.modules.sale.domain.SaleType;
import com.ddng.saleapi.modules.sale.dto.SaleDto;
import com.ddng.saleapi.modules.sale.dto.SaleItemDto;
import com.ddng.saleapi.modules.sale.repository.SaleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SaleControllerTest
{
    @Autowired WebApplicationContext ctx;
    @Autowired ObjectMapper objectMapper;
    @Autowired ModelMapper modelMapper;
    @Autowired MockMvc mockMvc;
    @Autowired ItemRepository itemRepository;
    @Autowired SaleRepository saleRepository;

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
    public void resetData()
    {
        saleRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void createSale() throws Exception
    {
        // given
        List<Item> allItem = itemRepository.findAll();

        SaleItemDto saleItemDto1 = SaleItemDto.builder()
                                                    .itemId(allItem.get(0).getId())
                                                    .count(2)
                                                .build();
        SaleItemDto saleItemDto2 = SaleItemDto.builder()
                                                    .itemId(allItem.get(1).getId())
                                                    .count(2)
                                                .build();

        SaleDto.Post saleDto = SaleDto.Post.builder()
                                                .familyId(1L)
                                                .payment(PaymentType.CARD)
                                                .saleItems(List.of(saleItemDto1, saleItemDto2))
                                                .type(SaleType.PAYED)
                                            .build();

        // when
        mockMvc.perform(
                        post("/sale")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saleDto))
                        )
                .andDo(print())
                .andExpect(status().isCreated());

        // then
        List<Sale> all = saleRepository.findAll();
        Sale sale = all.get(0);
        assertThat(all.size()).isEqualTo(1);
        assertThat(sale.getSaleItemList().size()).isEqualTo(2);

        System.out.println("payment is " + sale.getPayment());
        System.out.println("sale item is " + sale.getSaleItemList().get(0).getItem().getName());
        System.out.println("sale item price is " + sale.getSaleItemList().get(0).getSalePrice());
        System.out.println("sale item count is " + sale.getSaleItemList().get(0).getCount());
    }
}
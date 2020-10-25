package com.ddng.saleapi.modules.sale.controller;

import com.ddng.saleapi.modules.coupon.domain.Coupon;
import com.ddng.saleapi.modules.coupon.domain.CouponType;
import com.ddng.saleapi.modules.coupon.repository.CouponRepository;
import com.ddng.saleapi.modules.item.domain.Item;
import com.ddng.saleapi.modules.item.domain.ItemType;
import com.ddng.saleapi.modules.item.repository.ItemRepository;
import com.ddng.saleapi.modules.sale.domain.PaymentType;
import com.ddng.saleapi.modules.sale.domain.Sale;
import com.ddng.saleapi.modules.sale.domain.SaleType;
import com.ddng.saleapi.modules.sale.dto.SaleDto;
import com.ddng.saleapi.modules.sale.dto.SaleItemDto;
import com.ddng.saleapi.modules.sale.repository.SaleRepository;
import com.ddng.saleapi.modules.sale.service.SaleService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @Autowired CouponRepository couponRepository;
    @Autowired SaleService saleService;

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
        item2.setPrice(500);
        item2.setItemQuantity(100);

        itemRepository.save(item);
        itemRepository.save(item2);

        Coupon coupon = new Coupon();
        coupon.setExpireDate(LocalDateTime.now().plusDays(1));
        coupon.setCreateDate(LocalDateTime.now());
        coupon.setCustomerId(1L);
        coupon.setItemId(item.getId());
        coupon.setType(CouponType.DISCOUNT_ALL);

        couponRepository.save(coupon);
    }

    @AfterEach
    public void resetData()
    {
        saleRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    @DisplayName("판매 - 기본")
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

        List<Item> afterAllItems = itemRepository.findAll();
        assertThat(afterAllItems.get(0).getItemQuantity()).isLessThan(100);
        assertThat(afterAllItems.get(0).getItemQuantity()).isEqualTo(98);
        assertThat(afterAllItems.get(1).getItemQuantity()).isLessThan(100);
        assertThat(afterAllItems.get(1).getItemQuantity()).isEqualTo(98);

        System.out.println("payment is " + sale.getPayment());
        System.out.println("sale item is " + sale.getSaleItemList().get(0).getItem().getName());
        System.out.println("sale item price is " + sale.getSaleItemList().get(0).getSalePrice());
        System.out.println("sale item count is " + sale.getSaleItemList().get(0).getCount());
    }

    @Test
    @DisplayName("판매 - 쿠폰 적용")
    void createSale_with_coupon() throws Exception
    {
        // given
        List<Item> allItem = itemRepository.findAll();
        List<Coupon> allCoupons = couponRepository.findAll();

        // 1500 * 2
        SaleItemDto saleItemDto1 = SaleItemDto.builder()
                .itemId(allItem.get(0).getId())
                .count(2)
                .customerId(allCoupons.get(0).getCustomerId())
                .couponId(allCoupons.get(0).getId())
                .build();

        // 500 * 2
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
        assertThat(sale.getSaleItemList().get(0).getTotalPrice()).isEqualTo(1500);

        Optional<Coupon> optionalCoupon = couponRepository.findById(allCoupons.get(0).getId());
        assertThat(optionalCoupon.get().getUseDate()).isNotNull();

        List<Item> afterAllItems = itemRepository.findAll();
        assertThat(afterAllItems.get(0).getItemQuantity()).isLessThan(100);
        assertThat(afterAllItems.get(0).getItemQuantity()).isEqualTo(98);
        assertThat(afterAllItems.get(1).getItemQuantity()).isLessThan(100);
        assertThat(afterAllItems.get(1).getItemQuantity()).isEqualTo(98);
    }

    @Test
    @DisplayName("판매 검색 - 오늘 판매")
    void searchSale_today() throws Exception
    {
        // given
        List<Item> allItem = itemRepository.findAll();

        SaleItemDto saleItemDto1 = SaleItemDto.builder()
                .itemId(allItem.get(0).getId())
                .count(2)
                .build();

        // 500 * 2
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

        saleService.createSale(saleDto);


        SaleDto.Get dto = new SaleDto.Get();
        dto.setOnlyToday(true);

        // when
        ResultActions actions = mockMvc.perform(
                                                get("/sale")
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .content(objectMapper.writeValueAsString(dto))
                                                )
                                        .andDo(print())
                                        .andExpect(status().isOk());

        MockHttpServletResponse response = actions.andReturn().getResponse();
        String contentAsString = response.getContentAsString();
        JacksonJsonParser parser = new JacksonJsonParser();

        Map<String, Object> stringObjectMap = parser.parseMap(contentAsString);
        List<Object> content = (List<Object>) stringObjectMap.get("content");

        // then
        assertThat(content.size()).isEqualTo(1);
    }
}
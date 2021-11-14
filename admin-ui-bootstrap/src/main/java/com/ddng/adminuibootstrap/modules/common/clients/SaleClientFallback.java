package com.ddng.adminuibootstrap.modules.common.clients;

import com.ddng.adminuibootstrap.modules.common.dto.FeignPageImpl;
import com.ddng.adminuibootstrap.modules.common.dto.sale.CouponDto;
import com.ddng.adminuibootstrap.modules.common.dto.sale.ItemDto;
import com.ddng.adminuibootstrap.modules.common.dto.sale.ItemTypeDto;
import com.ddng.adminuibootstrap.modules.common.dto.sale.NewCouponDto;
import com.ddng.adminuibootstrap.modules.common.dto.sale.NewSaleDto;
import com.ddng.adminuibootstrap.modules.common.dto.sale.SaleDto;
import com.ddng.adminuibootstrap.modules.common.dto.sale.SaleItemDto;
import com.ddng.adminuibootstrap.modules.common.dto.sale.SaleType;
import com.ddng.adminuibootstrap.modules.item.form.EditForm;
import com.ddng.adminuibootstrap.modules.item.form.RegisterForm;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SaleClientFallback implements SaleClient{

    @Override
    public ItemDto getItem(Long itemId) {
        return null;
    }

    @Override
    public List<ItemDto> getHotelItems() {
        return Collections.emptyList();
    }

    @Override
    public List<ItemDto> getKindergartenItems() {
        return Collections.emptyList();
    }

    @Override
    public FeignPageImpl<ItemDto> getBeautyItemsWithPage(String keyword, int page, int size) {
        return new FeignPageImpl<>(Collections.emptyList(), 0, 0, 0);
    }

    @Override
    public FeignPageImpl<ItemDto> searchItemsWithPage(String keyword, int page, int size) {
        return new FeignPageImpl<>(Collections.emptyList(), 0, 0, 0);
    }

    @Override
    public List<ItemTypeDto> getItemTypes() {
        return Collections.emptyList();
    }

    @Override
    public ResponseEntity updateItem(Long itemId, EditForm editForm) {
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity createItem(RegisterForm registerForm) {
        return ResponseEntity.badRequest().build();
    }

    @Override
    public List<String> getBarcodes(int count) {
        return Collections.emptyList();
    }

    @Override
    public CouponDto getCoupon(Long couponId) {
        return null;
    }

    @Override
    public FeignPageImpl<CouponDto> getCoupons(List<Long> customerIds) {
        return new FeignPageImpl<>(Collections.emptyList(), 0, 0, 0);
    }

    @Override
    public List<Long> getCouponIssuableCustomers() {
        return Collections.emptyList();
    }

    @Override
    public ResponseEntity<String> issueNewCoupons(NewCouponDto newCouponDto) {
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<String> saleCart(NewSaleDto newSaleDto) {
        return ResponseEntity.badRequest().build();
    }

    @Override
    public List<SaleDto> getSaleHistoryByFamilyId(Long familyId) {
        return Collections.emptyList();
    }

    @Override
    public List<SaleItemDto> getSaleHistoryByCustomerId(Long customerId) {
        return Collections.emptyList();
    }

    @Override
    public List<SaleItemDto> getSaleHistoryByItemId(Long itemId) {
        return Collections.emptyList();
    }

    @Override
    public FeignPageImpl<SaleDto> searchSale(String salePeriodStart, String salePeriodEnd, SaleType saleType, int page, int size) {
        return new FeignPageImpl<>(Collections.emptyList(), 0, 0, 0);
    }

    @Override
    public SaleDto getSale(Long saleId) {
        return null;
    }

    @Override
    public ResponseEntity<String> refundSale(Long saleId) {
        return ResponseEntity.badRequest().build();
    }
}

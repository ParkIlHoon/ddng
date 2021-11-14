package com.ddng.adminuibootstrap.modules.common.clients;

import com.ddng.adminuibootstrap.modules.common.dto.FeignPageImpl;
import com.ddng.adminuibootstrap.modules.common.dto.customer.CustomerDto;
import com.ddng.adminuibootstrap.modules.common.dto.customer.CustomerTagDto;
import com.ddng.adminuibootstrap.modules.common.dto.customer.CustomerTypeDto;
import com.ddng.adminuibootstrap.modules.common.dto.customer.FamilyDto;
import com.ddng.adminuibootstrap.modules.customer.form.EditForm;
import com.ddng.adminuibootstrap.modules.customer.form.FamilySettingForm;
import com.ddng.adminuibootstrap.modules.customer.form.RegisterForm;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomerClientFallback implements CustomerClient {

    @Override
    public List<CustomerTypeDto> getCustomerTypes() {
        return Collections.emptyList();
    }

    @Override
    public FeignPageImpl<FamilyDto> searchFamilies(String keyword) {
        return new FeignPageImpl<>(Collections.emptyList(), 0, 0, 0);
    }

    @Override
    public FeignPageImpl<FamilyDto> searchFamiliesWithPage(String keyword, int page, int size) {
        return new FeignPageImpl<>(Collections.emptyList(), 0, 0, 0);
    }

    @Override
    public FamilyDto getFamily(Long familyId) {
        return null;
    }

    @Override
    public ResponseEntity createCustomer(RegisterForm registerForm) {
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity updateFamilySetting(Long familyId, FamilySettingForm familySettingForm) {
        return ResponseEntity.badRequest().build();
    }

    @Override
    public FeignPageImpl<CustomerDto> searchCustomersWithPage(String keyword, int page, int size) {
        return new FeignPageImpl<>(Collections.emptyList(), 0, 0, 0);
    }

    @Override
    public List<CustomerDto> getCustomers(List<Long> customerIds) {
        return Collections.emptyList();
    }

    @Override
    public CustomerDto getCustomer(Long customerId) {
        return null;
    }

    @Override
    public CustomerDto updateCustomer(Long customerId, EditForm editForm) {
        return null;
    }

    @Override
    public List<CustomerTagDto> getCustomerTags() {
        return Collections.emptyList();
    }

    @Override
    public ResponseEntity addCustomerTag(Long customerId, String title) {
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity removeCustomerTag(Long customerId, String title) {
        return ResponseEntity.badRequest().build();
    }
}

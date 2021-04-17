package com.ddng.customerapi.modules.family.dto;

import com.ddng.customerapi.modules.customer.dto.CustomerDto;
import com.ddng.customerapi.modules.family.domain.Family;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FamilyDto
{
    @Data @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "가족 생성 및 구성원 추가용 DTO")
    public static class Post
    {
        @ApiModelProperty(value = "고객 아이디", dataType = "Long", example = "1")
        private Long customerId;
    }

    @Data @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "가족 수정용 DTO")
    public static class Put
    {
        @ApiModelProperty(value = "가족 이름", dataType = "String", example = "치우네 가족")
        private String name;
    }

    @Data @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel(value = "가족 구성원 삭제용 DTO")
    public static class Delete
    {
        @ApiModelProperty(value = "고객 아이디", dataType = "Long", example = "1")
        private Long customerId;
    }


    @Data @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response
    {
        private Long id;
        private String name;
        private String text;
        private List<CustomerDto.Response> customers = new ArrayList<>();

        public Response(Family family)
        {
            this.id = family.getId();
            this.name = family.getName();
            this.text = family.getName() + "[" + family.getFamilyString() + "]";

            this.customers = family.getCustomers().stream().map(CustomerDto.Response::new).collect(Collectors.toList());
        }
    }

    @Data @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseWithCustomerTag
    {
        private Long id;
        private String name;
        private List<CustomerDto.Response> customers = new ArrayList<>();

        public ResponseWithCustomerTag(Family family)
        {
            this.id = family.getId();
            this.name = family.getName();

            this.customers = family.getCustomers().stream().map(CustomerDto.Response::new).collect(Collectors.toList());
        }
    }
}

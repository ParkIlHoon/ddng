package com.ddng.customerapi.modules.family.dto;

import com.ddng.customerapi.modules.customer.domain.Customer;
import com.ddng.customerapi.modules.customer.dto.CustomerDto;
import com.ddng.customerapi.modules.family.domain.Family;
import com.ddng.customerapi.modules.tag.domain.Tag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
//        private List<Map> customers = new ArrayList<>();
        private List<CustomerDto.Response> customers = new ArrayList<>();

        public Response(Family family)
        {
            this.id = family.getId();
            this.name = family.getName();
            this.text = family.getName() + "[" + family.getFamilyString() + "]";

            for (Customer customer : family.getCustomers())
            {
//                HashMap map = new HashMap();
//                map.put("id", customer.getId().toString());
//                map.put("name", customer.getName());
//                map.put("profileImg", customer.getProfileImg());
//                this.customers.add(map);
                this.customers.add(new CustomerDto.Response(customer));
            }
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

            for (Customer customer : family.getCustomers())
            {
                CustomerDto.Response dto = new CustomerDto.Response(customer);
//                HashMap map = new HashMap();
//                map.put("id", customer.getId().toString());
//                map.put("name", customer.getName());
//                map.put("telNo", customer.getTelNo());
//                map.put("type", customer.getType().getKorName());
//                map.put("profileImg", customer.getProfileImg());
//                map.put("tags", customer.getTags());

                this.customers.add(dto);
            }
        }
    }
}

package com.ddng.customerapi.modules.family.dto;

import com.ddng.customerapi.modules.customer.domain.Customer;
import com.ddng.customerapi.modules.family.domain.Family;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FamilyDto
{
    @Data @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Post
    {
        private Long customerId;
    }

    @Data @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Delete
    {
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
        private List<Map<String, String>> customers = new ArrayList<>();

        public Response(Family family)
        {
            this.id = family.getId();
            this.name = family.getName();
            this.text = family.getName() + "[" + family.getFamilyString() + "]";

            for (Customer customer : family.getCustomers())
            {
                HashMap map = new HashMap();
                map.put("id", customer.getId().toString());
                map.put("name", customer.getName());
                map.put("profileImg", customer.getProfileImg());
                this.customers.add(map);
            }
        }
    }
}

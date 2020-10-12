package com.ddng.customerapi.modules.family.dto;

import com.ddng.customerapi.modules.family.domain.Family;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

        public Response(Family family)
        {
            this.id = family.getId();
            this.name = family.getName();
            this.text = family.getName();
        }
    }
}

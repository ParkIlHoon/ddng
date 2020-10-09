package com.ddng.customerapi.modules.family.dto;

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
}

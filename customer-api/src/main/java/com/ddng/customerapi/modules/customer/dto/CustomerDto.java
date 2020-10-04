package com.ddng.customerapi.modules.customer.dto;

import com.ddng.customerapi.modules.customer.domain.CustomerType;
import lombok.*;

import java.time.LocalDateTime;

public class CustomerDto
{
    /**
     * <h1>응답용 고객 DTO</h1>
     */
    @Data @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response
    {
        private Long id;
        private String name;
        private String type;
        private String telNo;
        private LocalDateTime joinDate;
        private String bigo;
        private String profileImg;
        private String sexGb;
    }

    /**
     * <h1>POST 요청 처리용 고객 DTO</h1>
     */
    @Data @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Post
    {
        private String name;
        private CustomerType type;
        private String telNo;
        private String bigo;
        private String profileImg;
        private String sexGb;
    }

    /**
     * <h1>PUT 요청 처리용 고객 DTO</h1>
     */
    @Data @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Put
    {
        private String name;
        private CustomerType type;
        private String telNo;
        private String bigo;
        private String profileImg;
        private String sexGb;
    }
}

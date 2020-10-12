package com.ddng.customerapi.modules.customer.dto;

import com.ddng.customerapi.modules.customer.domain.Customer;
import com.ddng.customerapi.modules.customer.domain.CustomerType;
import com.ddng.customerapi.modules.family.domain.Family;
import com.ddng.customerapi.modules.tag.domain.Tag;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

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
        private String typeName;
        private String telNo;
        private LocalDateTime joinDate;
        private String bigo;
        private String profileImg;
        private String sexGb;
        private Set<Tag> tags;
        private Family family;
        private String familyString = "";

        public Response(Customer customer)
        {
            this.id = customer.getId();
            this.name = customer.getName();
            this.type = customer.getType().name();
            this.typeName = customer.getType().getKorName();
            this.telNo = customer.getTelNo();
            this.joinDate = customer.getJoinDate();
            this.bigo = customer.getBigo();
            this.profileImg = customer.getProfileImg();
            this.sexGb = customer.getSexGb();
            this.tags = customer.getTags();
            this.family = customer.getFamily();
        }

        public void createFamilyString()
        {
            if (this.family != null)
            {
                this.familyString = this.family.getFamilyString();
            }
        }
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
        private Long familyId;
        private LocalDateTime joinDate = LocalDateTime.now();
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
        private LocalDateTime joinDate;
    }
}

package com.ddng.customerapi.modules.customer.dto;

import com.ddng.customerapi.modules.customer.domain.Customer;
import com.ddng.customerapi.modules.customer.domain.CustomerType;
import com.ddng.customerapi.modules.family.domain.Family;
import com.ddng.customerapi.modules.tag.domain.Tag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
    @ApiModel("고객 조회용 DTO")
    public static class Response
    {
        @ApiModelProperty(value = "고객 아이디", dataType = "Long", example = "1")
        private Long id;
        @ApiModelProperty(value = "고객 이름", dataType = "String", example = "치우")
        private String name;
        @ApiModelProperty(value = "고객 견종 코드", dataType = "String", example = "MALTESE")
        private String type;
        @ApiModelProperty(value = "고객 견종 명칭", dataType = "String", example = "말티즈")
        private String typeName;
        @ApiModelProperty(value = "고객 전화번호", dataType = "String", example = "010-1234-5678")
        private String telNo;
        @ApiModelProperty(value = "고객 등록 일자", dataType = "String", example = "2021-01-14T14:22:05.098Z")
        private LocalDateTime joinDate;
        @ApiModelProperty(value = "비고", dataType = "String", example = "비고 내용")
        private String bigo;
        @ApiModelProperty(value = "고객 프로필 이미지 src", dataType = "String", example = "/fakePath/1.jpg")
        private String profileImg;
        @ApiModelProperty(value = "고객 성별", dataType = "String", example = "MALE / FEMALE")
        private String sexGb;
        @ApiModelProperty(value = "고객 태그", dataType = "Set")
        private Set<Tag> tags;
        @ApiModelProperty(value = "고객 소속 가족")
        private Family family;
        @ApiModelProperty(value = "고객 소속 가족 구성원")
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
    @ApiModel("고객 생성용 DTO")
    public static class Post
    {
        @ApiModelProperty(value = "고객 이름", dataType = "String", example = "치우")
        private String name;
        @ApiModelProperty(value = "고객 견종", dataType = "String", example = "MALTESE")
        private CustomerType type;
        @ApiModelProperty(value = "고객 전화번호", dataType = "String", example = "010-1234-5678")
        private String telNo;
        @ApiModelProperty(value = "비고", dataType = "String", example = "비고 내용")
        private String bigo;
        @ApiModelProperty(value = "고객 프로필 이미지", dataType = "String", example = "BASE64 String")
        private String profileImg;
        @ApiModelProperty(value = "고객 성별", dataType = "String", example = "MALE / FEMALE")
        private String sexGb;
        @ApiModelProperty(value = "소속 가족 아이디", dataType = "Long", example = "1")
        private Long familyId;
        @ApiModelProperty(value = "고객 등록 일자", dataType = "String", example = "2021-01-14T14:22:05.098Z")
        private LocalDateTime joinDate = LocalDateTime.now();
    }

    /**
     * <h1>PUT 요청 처리용 고객 DTO</h1>
     */
    @Data @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel("고객 수정용 DTO")
    public static class Put
    {
        @ApiModelProperty(value = "고객 이름", dataType = "String", example = "치우")
        private String name;
        @ApiModelProperty(value = "고객 견종", dataType = "String", example = "MALTESE")
        private CustomerType type;
        @ApiModelProperty(value = "고객 전화번호", dataType = "String", example = "010-1234-5678")
        private String telNo;
        @ApiModelProperty(value = "비고", dataType = "String", example = "비고 내용")
        private String bigo;
        @ApiModelProperty(value = "고객 프로필 이미지", dataType = "String", example = "BASE64 String")
        private String profileImg;
        @ApiModelProperty(value = "고객 성별", dataType = "String", example = "MALE / FEMALE")
        private String sexGb;
    }
}

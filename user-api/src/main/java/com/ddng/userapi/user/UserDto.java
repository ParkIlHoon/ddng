package com.ddng.userapi.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <h1>사용자 DTO 클래스</h1>
 *
 * 용도에 따라 본 클래스의 inner class 를 선택해 사용하도록 한다.
 * <ul>
 *     <li>Create : 사용자 생성 시 사용</li>
 *     <li>Update : 사용자 수정 시 사용</li>
 *     <li>Read : 사용자 조회 시 사용</li>
 * </ul>
 *
 * @author ParkIlHoon
 * @version 1.0
 */
public class UserDto
{
    /**
     * <h2>사용자 DTO Create 내부 클래스</h2>
     *
     * 사용자 생성 시 사용한다.(POST Request)
     *
     * @author ParkIlHoon
     * @version 1.0
     */
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Create
    {
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
        @NotEmpty
        private String name;
        private String email;
        private String telNo;
    }

    /**
     * <h2>사용자 DTO Update 내부 클래스</h2>
     *
     * 사용자 수정 시 사용한다.(PUT Request)
     *
     * @author ParkIlHoon
     * @version 1.0
     */
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Update
    {
        @NotEmpty
        private Long id;
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
        @NotEmpty
        private String name;
        private String email;
        private String telNo;
        private String imagePath;
        private Long teamId;
    }

    /**
     * <h2>사용자 DTO Read 내부 클래스</h2>
     *
     * 사용자 조회 시, ResponseEntity 구성할 때 사용한다.
     *
     * @author ParkIlHoon
     * @version 1.0
     * @see com.ddng.userapi.user.UserResource
     */
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Read
    {
        private Long id;
        private String username;
        private String password;
        private String name;
        private String email;
        private String telNo;
        private LocalDateTime joinDate;
        private String imagePath;
        private Long teamId;
        private List<Long> grants;
    }
}

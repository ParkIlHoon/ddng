package com.ddng.userapi.team;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <h1>팀 DTO 클래스</h1>
 *
 * 용도에 따라 본 클래스의 inner class 를 선택해 사용하도록 한다.
 * <ul>
 *     <li>Create : 팀 생성 시 사용</li>
 *     <li>Update : 팀 수정 시 사용</li>
 *     <li>Read : 팀 조회 시 사용</li>
 * </ul>
 *
 * @author ParkIlHoon
 * @version 1.0
 */
public class TeamDto
{
    /**
     * <h2>팀 DTO Create 내부 클래스</h2>
     *
     * 팀 생성 시 사용한다.(POST Request)
     *
     * @author ParkIlHoon
     * @version 1.0
     */
    @Data
    @Builder
    public static class Create
    {
        @NotBlank
        private String name;
        @NotBlank
        private String path;
        @NotNull
        private Long managerId;
    }

    /**
     * <h2>팀 DTO Update 내부 클래스</h2>
     *
     * 팀 수정 시 사용한다.(PUT Request)
     *
     * @author ParkIlHoon
     * @version 1.0
     */
    @Data
    @Builder
    public static class Update
    {
        @NotBlank
        private String name;
        private List<Long> managers;
        private List<Long> members;
    }

    /**
     * <h2>팀 DTO Read 내부 클래스</h2>
     *
     * 팀 조회 시, ResponseEntity 구성할 때 사용한다.
     *
     * @author ParkIlHoon
     * @version 1.0
     * @see com.ddng.userapi.team.TeamResource
     */
    @Data
    @Builder
    public static class Read
    {
        private Long id;
        private String path;
        private String name;
        private List<Long> managers;
        private List<Long> members;
    }
}

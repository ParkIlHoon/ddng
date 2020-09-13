package com.ddng.userapi.team;

import com.ddng.userapi.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>사용자 팀(그룹) 엔티티</h1>
 *
 * 팀 정보를 가지고 있는 엔티티 클래스
 *
 * @author ParkIlHoon
 * @version 1.0
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team
{
    /**
     * 팀 고유 아이디
     */
    @Id @GeneratedValue
    private Long id;

    /**
     * 팀 이름
     */
    private String name;

    /**
     * 팀 경로
     */
    private String path;

    /**
     * 생성 일자
     */
    private LocalDateTime createdDateTime;

    /**
     * 관리자 사용자
     */
    @ManyToMany(cascade = CascadeType.ALL)
    private List<User> managers = new ArrayList<>();

    /**
     * 소속 사용자
     */
    @OneToMany(cascade = CascadeType.ALL)
    private List<User> members = new ArrayList<>();

}

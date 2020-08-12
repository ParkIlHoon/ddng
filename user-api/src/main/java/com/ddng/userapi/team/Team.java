package com.ddng.userapi.team;

import com.ddng.userapi.user.User;
import lombok.*;

import javax.persistence.*;
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
@ToString(exclude = {"users"})
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
     * 소속 사용자
     */
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

}

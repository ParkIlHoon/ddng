package com.ddng.userapi.user;

import com.ddng.userapi.grant.Grant;
import com.ddng.userapi.team.Team;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>사용자 엔티티</h1>
 *
 * 사용자 정보를 가지고 있는 엔티티 클래스
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
@ToString(exclude = {"team", "grants"})
public class User
{
    /**
     * 사용자 고유 아이디
     */
    @Id @GeneratedValue
    private Long id;

    /**
     * 사용자 아이디. 로그인 시 사용
     */
    private String username;

    /**
     * 사용자 비밀번호. 로그인 시 사용
     */
    private String password;

    /**
     * 사용자 이름
     */
    private String name;

    /**
     * 사용자 이메일
     */
    private String email;

    /**
     * 전화번호
     */
    private String telNo;

    /**
     * 가입일
     */
    private LocalDateTime joinDate;

    /**
     * 이미지 경로
     */
    private String imagePath;

    /**
     * 소유 권한
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Grant> grants = new ArrayList<>();
}

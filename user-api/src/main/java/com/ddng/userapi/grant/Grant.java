package com.ddng.userapi.grant;

import com.ddng.userapi.role.Role;
import com.ddng.userapi.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * <h1>권한부여 엔티티</h1>
 *
 * 권한 부여 정보를 가지고 있는 엔티티 클래스
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
@ToString(exclude = {"user", "role"})
public class Grant
{
    /**
     * 권한 부여 고유 아이디
     */
    @Id @GeneratedValue
    private Long id;

    /**
     * 권한 부여 대상 사용자
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 부여 권한
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    /**
     * 권한 부여 일자
     */
    private LocalDateTime grantDate;

    /**
     * 권한 만료 일자
     */
    private LocalDateTime expireDate;

    /**
     * 권한 부여 상태
     */
    @Enumerated(EnumType.STRING)
    private GrantStatus status;


    /******************************************************************************************************************
     * 생성 메서드
     *****************************************************************************************************************/
    /**
     * 사용자에게 권한을 부여한다.
     * @param user 권한을 부여할 사용자
     * @param role 부여할 권한
     * @return 권한사용자 객체
     */
    public static Grant create(User user, Role role)
    {
        Grant roleUser = new Grant();

        roleUser.setUser(user);
        roleUser.setRole(role);
        roleUser.setGrantDate(LocalDateTime.now());
        roleUser.setStatus(GrantStatus.GRANTED);

        return roleUser;
    }

    /******************************************************************************************************************
     * 비즈니스 로직
     *****************************************************************************************************************/
    /**
     * 권한을 회수한다.
     */
    public void expire()
    {
        this.setStatus(GrantStatus.EXPIRED);
        this.setExpireDate(LocalDateTime.now());
    }

    /**
     * 권한을 재부여한다.
     */
    public void grant()
    {
        this.setStatus(GrantStatus.GRANTED);
        this.setGrantDate(LocalDateTime.now());
    }

    /**
     * 권한의 유효 여부를 반환한다.
     * @return 권한의 유효성
     */
    public boolean isGranted()
    {
        return this.getStatus().equals(GrantStatus.GRANTED);
    }
}

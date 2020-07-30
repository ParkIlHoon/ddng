package com.ddng.userapi.user;

import com.ddng.userapi.types.Address;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>사용자 엔티티</h1>
 *
 * 사용자 정보를 가지고 있는 엔티티 클래스
 *
 * @author ParkIlHoon
 * @version 1.0
 * @see com.ddng.userapi.user.UserRole
 * @see com.ddng.userapi.types.Address
 */
@Entity
public class User
{
    /**
     * 사용자 고유 아이디
     */
    @Id
    @GeneratedValue
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
     * 사용자 권한목록
     */
    @Enumerated(value = EnumType.STRING)
    private List<UserRole> userRoleList = new ArrayList();

    /**
     * 사용자 주소
     */
    @Embedded
    private Address address;


}

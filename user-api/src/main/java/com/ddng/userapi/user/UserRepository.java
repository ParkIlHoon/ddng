package com.ddng.userapi.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * <h1>User Spring Data JPA 리포지토리</h1>
 *
 * 사용자 관련 Spring Data JPA 에서 제공하는 메서드를 정의하는 인터페이스.
 * {@link com.ddng.userapi.user.UserRepositoryCustom} 에서 명세한 메서드를 지원한다.
 *
 * @author ParkIlHoon
 * @version 1.0
 * @see com.ddng.userapi.user.UserRepositoryCustom
 */
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom
{
}

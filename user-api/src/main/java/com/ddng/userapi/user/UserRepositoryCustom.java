package com.ddng.userapi.user;

import java.util.List;

/**
 * <h1>User 사용자정의 리포지토리 인터페이스</h1>
 *
 * Spring Data JPA 에서 지원하지 않는 쿼리를 정의하는 인터페이스.
 *
 * @author ParkIlHoon
 * @version 1.0
 * @see com.ddng.userapi.user.UserRepositoryImpl
 */
public interface UserRepositoryCustom
{
    /**
     * 사용자 목록을 검색한다.(QueryDsl)
     * @param dto 검색 조건
     * @return 검색 조건에 해당하는 사용자 DTO 목록
     */
    List<UserDto.Read> search(UserDto.Read dto);
}

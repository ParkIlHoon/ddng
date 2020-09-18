package com.ddng.userapi.team;

import java.util.List;

/**
 * <h1>Team 사용자정의 리포지토리 인터페이스</h1>
 *
 * Spring Data JPA 에서 지원하지 않는 쿼리를 정의하는 인터페이스.
 *
 * @author ParkIlHoon
 * @version 1.0
 * @see com.ddng.userapi.user.UserRepositoryImpl
 */
public interface TeamRepositoryCustom
{
    List<Team> searchEq (TeamDto.Read dto);
}

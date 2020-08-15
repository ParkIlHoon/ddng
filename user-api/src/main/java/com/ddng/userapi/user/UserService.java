package com.ddng.userapi.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <h1>User 서비스</h1>
 *
 * @author ParkIlHoon
 * @version 1.0
 */
@Service
public class UserService
{
    @Autowired
    UserRepository userRepository;

    /**
     * 사용자 한 명을 조회한다
     * @param id 조회할 사용자의 아이디
     * @return 아이디에 해당하는 User 객체
     */
    public Optional<User> findById(Long id)
    {
        return userRepository.findById(id);
    }

    /**
     * 사용자를 저장한다
     * @param user 저장할 사용자
     * @return 저장된 사용자 User 객체
     */
    public User save(User user)
    {
        return userRepository.save(user);
    }

    /**
     * 사용자를 검색한다.
     * @param dto 조회 조건
     * @return 조회 조건에 해당하는 사용자 DTO 목록
     */
    public List<UserDto.Read> search(UserDto.Read dto)
    {
        return userRepository.search(dto);
    }
}

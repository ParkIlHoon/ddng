package com.ddng.userapi.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * <h1>User 서비스</h1>
 *
 * @author ParkIlHoon
 * @version 1.0
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserService
{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

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
     * 사용자를 검색한다.(eq조건)
     * @param dto 조회 조건
     * @return 조회 조건에 해당하는 사용자 DTO 목록
     */
    public List<UserDto.Read> searchEq(UserDto.Read dto)
    {
        return userRepository.search(dto);
    }

    /**
     * 사용자 신규 생성
     * @param dto 새로 생성할 사용자의 정보
     * @return
     */
    public User createUser(UserDto.Create dto)
    {
        // DTO -> Entity 변환
        User mapped = modelMapper.map(dto, User.class);
        mapped.setJoinDate(LocalDateTime.now());
        return userRepository.save(mapped);
    }

    /**
     * 사용자 정보를 수정한다.
     * @param user 수정할 사용자 객체
     * @param dto
     * @return
     */
    public User updateUser(User user, UserDto.Update dto)
    {
        modelMapper.map(dto, user);
        return userRepository.save(user);
    }

    /**
     * 사용자를 제거한다.
     * @param user 제거할 사용자
     */
    public void deleteUser(User user)
    {
        userRepository.delete(user);
    }
}

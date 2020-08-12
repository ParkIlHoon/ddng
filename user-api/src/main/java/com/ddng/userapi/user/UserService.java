package com.ddng.userapi.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public User save(User user)
    {
        return userRepository.save(user);
    }
}

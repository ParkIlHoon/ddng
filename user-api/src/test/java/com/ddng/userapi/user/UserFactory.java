package com.ddng.userapi.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <h1>User 테스트를 위한 사용자 생성 팩토리 클래스</h1>
 */
@Component
@RequiredArgsConstructor
public class UserFactory
{
    @Autowired
    private UserService userService;

    public User createUser (long id)
    {
        UserDto.Create userDto = new UserDto.Create();

        userDto.setUsername("username_" + id);
        userDto.setName("name_" + id);
        userDto.setPassword("1234");
        userDto.setEmail("username_" + id + "@gmail.com");
        userDto.setTelNo("01012345678");

        return userService.createUser(userDto);
    }

}

package com.ddng.userapi.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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

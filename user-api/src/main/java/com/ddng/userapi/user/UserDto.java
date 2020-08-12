package com.ddng.userapi.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto
{
    private String username;

    private String password;

    private String name;

    private String email;

    private String telNo;

    private LocalDateTime joinDate;

    private String imagePath;
}

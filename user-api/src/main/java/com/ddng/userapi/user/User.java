package com.ddng.userapi.user;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "USER")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NICKNAME")
    private String nickName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TEL_NO")
    private String telNo;

    @Column(name = "JOIN_DATE")
    private LocalDateTime joinDate;

    @Column(name = "EMAIL")
    private String email;

    protected User() { }
}

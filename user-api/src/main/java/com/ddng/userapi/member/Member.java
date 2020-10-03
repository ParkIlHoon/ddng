package com.ddng.userapi.member;

import com.ddng.userapi.group.Group;
import com.ddng.userapi.user.User;

import javax.persistence.*;

@Entity
@Table(name = "MEMBER")
public class Member
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    protected Member() { }
}

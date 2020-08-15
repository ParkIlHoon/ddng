package com.ddng.userapi.user;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * <h1>사용자 HATEOAS 처리를 위한 클래스</h1>
 *
 * @author ParkIlHoon
 * @version 1.0
 */
@Getter
public class UserResource extends RepresentationModel
{
    @JsonUnwrapped
    private UserDto.Read user;

    public UserResource(User u)
    {
        this.user = UserDto.Read.builder()
                                .id(u.getId())
                                .username(u.getUsername())
                                .password(u.getPassword())
                                .name(u.getName())
                                .email(u.getEmail())
                                .joinDate(u.getJoinDate())
                                .telNo(u.getTelNo())
                                .imagePath(u.getImagePath())
                                .teamId((u.getTeam() != null)? u.getTeam().getId() : null)
                                .grants((u.getGrants() != null && u.getGrants().size() > 0)?
                                        u.getGrants().stream().map(grant -> grant.getId()).collect(Collectors.toList())
                                        : null
                                        )
                            .build();
        // 자신에 대한 self 링크를 자동으로 생성하도록 처리
        this.add(linkTo(UserController.class).slash(u.getId()).withSelfRel());
    }

    public UserResource(UserDto.Read dto)
    {
        this.user = dto;
        // 자신에 대한 self 링크를 자동으로 생성하도록 처리
        this.add(linkTo(UserController.class).slash(dto.getId()).withSelfRel());
    }
}

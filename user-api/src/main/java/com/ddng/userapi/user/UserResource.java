package com.ddng.userapi.user;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * <h1>HATEOAS 처리를 위한 클래스</h1>
 */
@Getter
public class UserResource extends RepresentationModel
{
    @JsonUnwrapped
    private User user;

    public UserResource(User u)
    {
        this.user = u;
        // 자신에 대한 self 링크를 자동으로 생성하도록 처리
        this.add(linkTo(UserController.class).slash(u.getId()).withSelfRel());
    }
}

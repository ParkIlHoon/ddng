package com.ddng.userapi.team;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * <h1>팀 HATEOAS 처리를 위한 클래스</h1>
 *
 * @author ParkIlHoon
 * @version 1.0
 */
@Getter
public class TeamResource extends RepresentationModel
{
    @JsonUnwrapped
    private TeamDto.Read dto;

    public TeamResource (Team team)
    {
        this.dto = TeamDto.Read.builder()
                                    .id(team.getId())
                                    .name(team.getName())
                                    .path(team.getPath())
                                    .managers(team.getManagers().stream().map(manager -> manager.getId()).collect(Collectors.toList()))
                                    .members(team.getMembers().stream().map(member -> member.getId()).collect(Collectors.toList()))
                                .build();

        this.add(linkTo(TeamController.class).slash(team.getId()).withSelfRel());
    }
}

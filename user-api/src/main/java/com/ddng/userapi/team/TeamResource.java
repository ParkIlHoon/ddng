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
        this.dto = new TeamDto.Read();
        this.dto.setId(team.getId());
        this.dto.setName(team.getName());
        this.dto.setPath(team.getPath());
        this.dto.setManagers(team.getManagers().stream().map(manager -> manager.getId()).collect(Collectors.toList()));
        this.dto.setMembers(team.getMembers().stream().map(member -> member.getId()).collect(Collectors.toList()));
        this.add(linkTo(TeamController.class).slash(team.getId()).withSelfRel());
    }
}

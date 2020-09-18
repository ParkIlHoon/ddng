package com.ddng.userapi.team;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * <h1>팀 관련 요청 처리 컨트롤러</h1>
 *
 * @author ParkIlHoon
 * @version 1.0
 */
@RestController
@RequestMapping(value = "/teams", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class TeamController
{
    private final TeamService teamService;

    /**
     * 팀 신규 생성 요청 처리 메서드
     * @param dto 신규 생성할 팀 정보
     * @param errors
     * @return
     */
    @PostMapping
    public ResponseEntity createTeam (@RequestBody @Valid TeamDto.Create dto, Errors errors)
    {
        if (errors.hasErrors())
        {
            return ResponseEntity.badRequest().build();
        }

        Team team = teamService.createTeam(dto);

        // uri 생성
        WebMvcLinkBuilder builder = linkTo(TeamController.class).slash(team.getId());
        URI uri = builder.toUri();

        // HATEOAS 처리
        TeamResource resource = new TeamResource(team);
        resource.add(linkTo(TeamController.class).withRel("query-teams"));
        resource.add(builder.withRel("update-team"));
        resource.add(builder.withRel("delete-team"));
        resource.add(new Link("/docs/index.html#resources-teams-create").withRel("profile"));


        return ResponseEntity.created(uri).body(resource);
    }

    /**
     * 아이디로 team 을 조회한다
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity getTeam (@PathVariable Long id)
    {
        Optional<Team> team = teamService.findTeam(id);

        if (team.isEmpty())
        {
            return ResponseEntity.notFound().build();
        }

        // uri 생성
        WebMvcLinkBuilder builder = linkTo(TeamController.class).slash(team.get().getId());

        // HATEOAS 처리
        TeamResource resource = new TeamResource(team.get());
        resource.add(linkTo(TeamController.class).withRel("query-teams"));
        resource.add(builder.withRel("update-team"));
        resource.add(builder.withRel("delete-team"));
        resource.add(new Link("/docs/index.html#resources-get-team").withRel("profile"));

        return ResponseEntity.ok(resource);
    }

    @GetMapping
    public ResponseEntity queryTeams (@ModelAttribute TeamDto.Read dto)
    {
        List<TeamDto.Read> teams = teamService.searchEq(dto);
        WebMvcLinkBuilder builder = linkTo(TeamController.class);
        return ResponseEntity.ok(teams);
    }
}

package com.ddng.userapi.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TeamFactory
{
    @Autowired
    private TeamRepository teamRepository;

    public Team createTeam(String name)
    {
        Team team = new Team();
        team.setName(name);
        team.setPath("team-" + name);
        team.setCreatedDateTime(LocalDateTime.now());

        return teamRepository.save(team);
    }


}

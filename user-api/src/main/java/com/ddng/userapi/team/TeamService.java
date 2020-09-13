package com.ddng.userapi.team;

import com.ddng.userapi.user.User;
import com.ddng.userapi.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <h1>Team 서비스</h1>
 *
 * @author ParkIlHoon
 * @version 1.0
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TeamService
{
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    /**
     * 팀을 신규 생성한다.
     * @param dto 신규 생성할 팀 정보
     * @return 신규 생성된 Team 객체
     */
    public Team createTeam(TeamDto.Create dto)
    {
        int countByPath = teamRepository.countByPath(dto.getPath());

        if (countByPath > 0)
        {
            throw new IllegalArgumentException("경로가 중복됩니다.");
        }

        // 팀 생성
        Team team = modelMapper.map(dto, Team.class);
        team.setCreatedDateTime(LocalDateTime.now());
        Team newTeam = teamRepository.save(team);
        // 매니저 세팅
        User user = userRepository.findById(dto.getManagerId()).orElseThrow();
        newTeam.getManagers().add(user);
        return newTeam;
    }

}

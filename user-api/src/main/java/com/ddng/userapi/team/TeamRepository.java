package com.ddng.userapi.team;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long>
{
    int countByPath(String path);
}

package com.ddng.userapi.team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface TeamRepository extends JpaRepository<Team, Long>, TeamRepositoryCustom
{
    int countByPath(String path);
}

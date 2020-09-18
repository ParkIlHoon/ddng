package com.ddng.userapi.team;

import com.ddng.userapi.user.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ddng.userapi.team.QTeam.team;
import static org.springframework.util.StringUtils.hasText;

@Transactional(readOnly = true)
public class TeamRepositoryImpl extends QuerydslRepositorySupport implements TeamRepositoryCustom
{
    public TeamRepositoryImpl()
    {
        super(Team.class);
    }

    @Override
    public List<Team> searchEq(TeamDto.Read dto)
    {
        //FIXME
        QUser manager = QUser.user;
        QUser member = QUser.user;

        JPQLQuery<Team> query = from(team)
                                .where(
                                        idEq(dto.getId()),
                                        pathEq(dto.getPath()),
                                        nameEq(dto.getName())
                                )
                                .leftJoin(team.managers, manager).fetchJoin()
                                .leftJoin(team.members, member).fetchJoin()
                                .distinct();

        return query.fetch();
    }

    private BooleanExpression idEq(Long id)
    {
        return id != null? team.id.eq(id) : null;
    }

    private BooleanExpression pathEq(String path)
    {
        return hasText(path)? team.path.eq(path) : null;
    }

    private BooleanExpression nameEq(String name)
    {
        return hasText(name)? team.name.eq(name) : null;
    }
}

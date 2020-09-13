package com.ddng.userapi.user;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static com.ddng.userapi.team.QTeam.team;
import static com.ddng.userapi.user.QUser.user;
import static org.springframework.util.StringUtils.hasText;

/**
 * <h1>User 사용자정의 리포지토리 구현체</h1>
 *
 * Spring Data JPA 에서 지원하지 않는 쿼리를 구현하는 클래스.
 *
 * @author ParkIlHoon
 * @version 1.0
 * @see com.ddng.userapi.user.UserRepositoryCustom
 */
@Transactional(readOnly = true)
public class UserRepositoryImpl implements UserRepositoryCustom
{
    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(EntityManager entityManager)
    {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<UserDto.Read> search(UserDto.Read dto)
    {
        return queryFactory.select(new QUserDto_Read(user.id, user.username, user.password, user.name,
                                                    user.email, user.telNo, user.joinDate, user.imagePath))
                            .from(user)
                            .where(
                                    userIdEq(dto.getId()),
                                    userNameEq(dto.getUsername()),
                                    passwordEq(dto.getPassword()),
                                    nameEq(dto.getName()),
                                    emailEq(dto.getEmail()),
                                    telNoEq(dto.getTelNo()),
                                    joinDateEq(dto.getJoinDate()),
                                    imagePathEq(dto.getImagePath())
                                    )
                            .fetch();
    }

    private BooleanExpression imagePathEq(String imagePath)
    {
        return hasText(imagePath)? user.imagePath.eq(imagePath) : null;
    }

    private BooleanExpression joinDateEq(LocalDateTime joinDate)
    {
        return joinDate != null? user.joinDate.eq(joinDate) : null;
    }

    private BooleanExpression telNoEq(String telNo)
    {
        return hasText(telNo)? user.telNo.eq(telNo) : null;
    }

    private BooleanExpression emailEq(String email)
    {
        return hasText(email)? user.email.eq(email) : null;
    }

    private BooleanExpression nameEq(String name)
    {
        return hasText(name)? user.name.eq(name) : null;
    }

    private BooleanExpression passwordEq(String password)
    {
        return hasText(password)? user.password.eq(password) : null;
    }

    private BooleanExpression userNameEq(String username)
    {
        return hasText(username)? user.username.eq(username) : null;
    }

    private Predicate userIdEq(Long id)
    {
        return id != null? user.id.eq(id) : null;
    }
}

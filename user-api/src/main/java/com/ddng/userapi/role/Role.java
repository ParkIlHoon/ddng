package com.ddng.userapi.role;

import com.ddng.userapi.grant.Grant;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>권한 엔티티</h1>
 *
 * 권한 정보를 가지고 있는 엔티티 클래스
 *
 * @author ParkIlHoon
 * @version 1.0
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "grants")
public class Role
{
    /**
     * 권한 고유 아이디
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 권한명
     */
    private String name;

    /**
     * 부여 내역
     */
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<Grant> grants = new ArrayList<>();
}

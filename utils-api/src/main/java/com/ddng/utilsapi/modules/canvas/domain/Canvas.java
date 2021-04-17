package com.ddng.utilsapi.modules.canvas.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * <h1>캔버스 엔티티 클래스</h1>
 *
 * @version 1.0
 */
@Entity
@Table(name = "CANVAS")
@Builder @Getter @Setter
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
public class Canvas
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "IS_TOP_FIXED")
    private boolean isTopFixed;

    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<CanvasTag> tags = new HashSet<>();
}

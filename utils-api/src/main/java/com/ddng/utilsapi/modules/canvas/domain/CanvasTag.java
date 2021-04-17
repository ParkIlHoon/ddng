package com.ddng.utilsapi.modules.canvas.domain;

import lombok.*;

import javax.persistence.*;

/**
 * <h1>캔버스 태그 엔티티 클래스</h1>
 *
 * @version 1.0
 */
@Entity
@Table(name = "CANVAS_TAG")
@Builder @Getter @Setter
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
public class CanvasTag
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;
}

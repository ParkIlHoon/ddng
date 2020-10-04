package com.ddng.customerapi.modules.tag.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "TAG")
@Builder @Getter @Setter
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
public class Tag
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    protected Tag() { }
}

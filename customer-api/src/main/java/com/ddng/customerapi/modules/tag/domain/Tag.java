package com.ddng.customerapi.modules.tag.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "TAG")
@Builder @Getter @Setter
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor @NoArgsConstructor
public class Tag
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;
}

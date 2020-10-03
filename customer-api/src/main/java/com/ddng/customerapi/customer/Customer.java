package com.ddng.customerapi.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CUSTOMER")
@Builder @Getter
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
public class Customer
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private CustomerType type;

    @Column(name = "TEL_NO")
    private String telNo;

    @Column(name = "JOIN_DATE")
    private LocalDateTime joinDate;

    @Lob
    @Column(name = "BIGO")
    private String bigo;

    @Lob
    @Column(name = "PROFILE_IMG")
    private String profileImg;

    @Column(name = "SEX_GB")
    private String sexGb;

    protected Customer() { }
}

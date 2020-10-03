package com.ddng.customerapi.customer;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CUSTOMER")
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

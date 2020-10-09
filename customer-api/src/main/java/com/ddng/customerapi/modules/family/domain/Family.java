package com.ddng.customerapi.modules.family.domain;

import com.ddng.customerapi.modules.customer.domain.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FAMILY")
@Getter
@EqualsAndHashCode(of = {"id"})
public class Family
{
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @OneToMany(mappedBy = "family")
    @JsonIgnore
    private List<Customer> customers = new ArrayList<>();


    public Family (Customer customer)
    {
        this.customers.add(customer);
        customer.setFamily(this);
        this.name = customer.getName() + "(이)네 가족";
    }

    public void addMember (Customer customer)
    {
        this.customers.add(customer);
        customer.setFamily(this);
    }

    public void addMembers (List<Customer> customers)
    {
        for (Customer customer : customers)
        {
            this.customers.add(customer);
            customer.setFamily(this);
        }
    }

    public void removeMember (Customer customer)
    {
        this.customers.remove(customer);
        customer.setFamily(null);
    }

    public void changeName ()
    {
        String customerName = this.customers.get(0).getName();
        this.name = customerName + "(이)네 가족";
    }

    protected Family ()
    {

    }
}

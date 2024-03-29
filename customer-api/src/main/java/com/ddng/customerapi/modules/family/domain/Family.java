package com.ddng.customerapi.modules.family.domain;

import com.ddng.customerapi.modules.customer.domain.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "FAMILY")
@Getter @Setter
@EqualsAndHashCode(of = {"id"})
public class Family
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public void removeAllMember ()
    {
        this.customers.stream().forEach(customer -> customer.setFamily(null));
        this.customers = new ArrayList<>();
    }

    public void changeName ()
    {
        String customerName = this.customers.get(0).getName();
        this.name = customerName + "(이)네 가족";
    }

    public String getFamilyString()
    {
        String returnText = "";
        for (Customer customer : this.customers)
        {
            returnText += customer.getName() + "(" + customer.getType().getKorName() + " / " + customer.getTelNo() + ")\n";
        }
        return returnText;
    }

    protected Family ()
    {

    }
}

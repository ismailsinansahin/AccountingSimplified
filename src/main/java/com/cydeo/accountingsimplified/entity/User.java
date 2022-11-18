package com.cydeo.accountingsimplified.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Where(clause = "is_deleted=false")
public class User extends BaseEntity{

    @Column(unique = true)
    private String username;

    private String password;

    private String firstname;

    private String lastname;

    private String phone;

    private boolean enabled;

    @ManyToOne
//    @JoinColumn(name = "role_id") not mandatory. by default : role_id
    private Role role;

    @ManyToOne
//    @JoinColumn(name = "company_id") not mandatory. by default : company_id
    private Company company;

}


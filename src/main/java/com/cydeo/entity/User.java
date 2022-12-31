package com.cydeo.entity;

import com.cydeo.entity.common.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "users")
@Where(clause = "is_deleted=false")
public class User extends BaseEntity {

    //@NotBlank controller'da varsa, burada gerek yok bence
    @Column(unique = true, nullable = false)
    private String username;

    //@NotBlank controller'da varsa, burada gerek yok bence
    @Column(nullable = false)
    private String password;

    private String firstname;

    private String lastname;

    private String phone;

    private boolean enabled;

    @ManyToOne
    private Role role;

    @ManyToOne
    private Company company;

}


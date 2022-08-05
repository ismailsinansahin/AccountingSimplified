package com.cydeo.accountingsimplified.entity;

import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
@Where(clause = "is_deleted = false")
public class Role extends BaseEntity {

    @NotNull
    private String role;

}

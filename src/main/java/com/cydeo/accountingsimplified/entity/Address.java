package com.cydeo.accountingsimplified.entity;

import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses")
@Where(clause = "is_deleted=false")
public class Address extends BaseEntity{

    @NotNull
    private String addressLine1;

    private String addressLine2;

    @NotNull
    private String city;

    @NotNull
    private String state;

    @NotNull
    private String country;

    @NotNull
    private String zipCode;

    @OneToOne(mappedBy = "address")
    private Company company;

    @OneToOne(mappedBy = "address")
    private ClientVendor clientVendor;

}

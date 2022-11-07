package com.cydeo.accountingsimplified.entity;

import com.cydeo.accountingsimplified.enums.CompanyStatus;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "companies")
@Where(clause = "is_deleted=false")
public class Company extends BaseEntity{

    @NotNull
    private String title;

    @NotNull
    private String phone;

    private String website;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CompanyStatus companyStatus;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name="address_id")
    private Address address;

}

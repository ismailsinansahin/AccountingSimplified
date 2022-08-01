package com.cydeo.accountingsimplified.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "companies")
@Where(clause = "is_deleted=false")
public class Company extends BaseEntity{

    private String title;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="address_id")
    private Address HeadquarterAddress;

    private String phone;

    private String webSite;

    @Enumerated(EnumType.STRING)
    private LocalDate establishmentDate;

    @Enumerated(EnumType.STRING)
    private CompanyStatus companyStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    private User admin;

}

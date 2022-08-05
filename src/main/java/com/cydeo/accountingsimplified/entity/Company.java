package com.cydeo.accountingsimplified.entity;

import com.cydeo.accountingsimplified.enums.CompanyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "companies")
@Where(clause = "is_deleted=false")
public class Company extends BaseEntity{

    private String title;
    private String phone;
    private String website;
    private LocalDate establishmentDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="address_id")
    private Address address;

    @Enumerated(EnumType.STRING)
    private CompanyStatus companyStatus;

//    @OneToMany(mappedBy = "company",fetch = FetchType.LAZY)
//    private List<User> user;

}

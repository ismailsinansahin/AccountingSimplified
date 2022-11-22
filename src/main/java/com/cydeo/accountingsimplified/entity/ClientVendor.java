package com.cydeo.accountingsimplified.entity;

import com.cydeo.accountingsimplified.enums.ClientVendorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients_vendors")
@Where(clause = "is_deleted=false")
public class ClientVendor extends BaseEntity{

   @Column(unique = true)
    private String companyName;

    private String phone;

    private String website;


    @Enumerated(EnumType.STRING)
    private ClientVendorType clientVendorType;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="address_id")
    private Address address;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

}

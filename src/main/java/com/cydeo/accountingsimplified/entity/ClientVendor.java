package com.cydeo.accountingsimplified.entity;

import com.cydeo.accountingsimplified.enums.ClientVendorType;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients_vendors")
@Where(clause = "is_deleted=false")
public class ClientVendor extends BaseEntity{


    private String clientVendorName;

    private String phone;

    private String website;

    @Enumerated(EnumType.STRING)
    private ClientVendorType clientVendorType;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Address address;


    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

}

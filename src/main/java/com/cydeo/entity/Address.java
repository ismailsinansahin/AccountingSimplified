package com.cydeo.entity;

import com.cydeo.entity.common.BaseEntity;
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
public class Address extends BaseEntity {

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String country;

    private String zipCode;

    // if we create bidirectional relationship, Cascade.PERSIST, Merge doesn't work properly
//    @OneToOne(mappedBy = "address")
//    private Company company;
//
//
//    @OneToOne(mappedBy = "address")
//    private ClientVendor clientVendor;

}

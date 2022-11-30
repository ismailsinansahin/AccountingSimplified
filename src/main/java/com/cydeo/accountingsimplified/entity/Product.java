package com.cydeo.accountingsimplified.entity;

import com.cydeo.accountingsimplified.enums.ProductUnit;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
@Where(clause = "is_deleted=false")
public class Product extends BaseEntity{

    private String name;

    private int quantityInStock;

    private int lowLimitAlert;

    @Enumerated(EnumType.STRING)
    private ProductUnit productUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

}

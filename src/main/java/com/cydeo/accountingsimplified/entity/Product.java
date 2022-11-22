package com.cydeo.accountingsimplified.entity;

import com.cydeo.accountingsimplified.enums.ProductUnit;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @NotNull
    private String name;

//    @NotNull // instead of this, int primitive type can be used
    private int quantityInStock;

//    @NotNull // instead of this, int primitive type can be used
    private int lowLimitAlert;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProductUnit productUnit;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

}

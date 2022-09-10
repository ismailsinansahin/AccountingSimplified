package com.cydeo.accountingsimplified.entity;

import com.cydeo.accountingsimplified.enums.ProductUnit;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
@Where(clause = "is_deleted=false")
public class Product extends BaseEntity{

    @NotNull
    private String name;

    private Integer quantityInStock;

    private Integer quantityInInvoice;

    @NotNull
    private Integer lowLimitAlert;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProductUnit productUnit;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

}

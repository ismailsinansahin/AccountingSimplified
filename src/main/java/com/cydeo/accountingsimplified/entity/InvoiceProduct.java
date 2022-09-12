package com.cydeo.accountingsimplified.entity;

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
@Table(name = "invoice_products")
@Where(clause = "is_deleted=false")
public class InvoiceProduct extends BaseEntity{

    private Integer quantity;
    private Integer price;
    private Integer tax;
    private Integer amount;
    private Integer profitLoss;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

}

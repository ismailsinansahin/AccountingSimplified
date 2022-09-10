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

    private int quantityInInvoice;
    private int purchasePrice;
    private int purchaseTax;
    private int purchaseCost;
    private int saledPrice;
    private int saledTax;
    private int saledAmount;
    private int profitLoss;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

}

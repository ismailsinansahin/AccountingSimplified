package com.cydeo.accountingsimplified.entity;

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
@Table(name = "invoice_products")
@Where(clause = "is_deleted=false")
public class InvoiceProduct extends BaseEntity{

    private int quantity;
    private BigDecimal price;
    private int tax;
    private BigDecimal profitLoss;
    private int remainingQuantity;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Override
    public String toString() {
        return "InvoiceProduct{" +
                "quantity=" + quantity +
                ", price=" + price +
                ", tax=" + tax +
                ", profitLoss=" + profitLoss +
                ", remainingQuantity=" + remainingQuantity +
                ", invoice=" + invoice +
                ", product.name=" + product.getName() +
                ", product.quantity=" + product.getQuantityInStock() +
                '}';
    }
}

package com.cydeo.accountingsimplified.entity;

import com.cydeo.accountingsimplified.enums.InvoiceStatus;
import com.cydeo.accountingsimplified.enums.InvoiceType;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invoices")
@Where(clause = "is_deleted=false")
public class Invoice extends BaseEntity{

    @NotNull
    private String invoiceNo;

    @NotNull
    @Enumerated(EnumType.STRING)
    private InvoiceStatus invoiceStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    private InvoiceType invoiceType;

    @NotNull
    @DateTimeFormat
    private LocalDate date;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "client_vendor_id")
    private ClientVendor clientVendor;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY)
    private List<InvoiceProduct> invoiceProductList = new ArrayList<>();

}

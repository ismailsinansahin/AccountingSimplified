package com.cydeo.accountingsimplified.entity;

import com.cydeo.accountingsimplified.enums.InvoiceStatus;
import com.cydeo.accountingsimplified.enums.InvoiceType;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invoices")
@Where(clause = "is_deleted=false")
public class Invoice extends BaseEntity{

    private String invoiceNo;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus invoiceStatus;

    @Enumerated(EnumType.STRING)
    private InvoiceType invoiceType;

    @DateTimeFormat
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "client_vendor_id")
    private ClientVendor clientVendor;
}

package com.cydeo.accountingsimplified.entity;

import com.cydeo.accountingsimplified.enums.Months;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="payment")
@Where(clause = "is_deleted=false")
public class Payment extends BaseEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Enumerated(EnumType.STRING)
    private Months month;

    @Column(columnDefinition = "DATE")
    private LocalDate year;

    private int amount;
    private boolean isPaid;




}
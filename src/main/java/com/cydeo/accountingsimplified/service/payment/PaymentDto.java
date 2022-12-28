package com.cydeo.accountingsimplified.service.payment;

import com.cydeo.accountingsimplified.dto.CompanyDto;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PaymentDto {
    private Long id;
    private CompanyDto company;
    private Months month;
    private LocalDate year;
    private int amount;
    private boolean isPaid;
}

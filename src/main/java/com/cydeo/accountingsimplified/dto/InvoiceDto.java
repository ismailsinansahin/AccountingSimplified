package com.cydeo.accountingsimplified.dto;

import com.cydeo.accountingsimplified.enums.InvoiceStatus;
import com.cydeo.accountingsimplified.enums.InvoiceType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceDto {

    private Long id;

    private String invoiceNo;

    private InvoiceStatus invoiceStatus;

    private InvoiceType invoiceType;

    @DateTimeFormat(pattern = "MMMM dd, yyyy")
    private LocalDate date;

    private CompanyDto company;

    @NotNull(message = "This is a required field.")
    @Valid
    private ClientVendorDto clientVendor;

    private BigDecimal price;
    private BigDecimal tax;
    private BigDecimal total;

    private List<InvoiceProductDto> invoiceProducts;

}

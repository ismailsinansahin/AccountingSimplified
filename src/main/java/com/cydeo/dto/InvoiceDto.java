package com.cydeo.dto;

import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

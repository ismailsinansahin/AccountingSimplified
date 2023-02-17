package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.InvoiceDto;
import com.cydeo.accountingsimplified.dto.InvoiceProductDto;
import com.cydeo.accountingsimplified.enums.InvoiceStatus;
import com.cydeo.accountingsimplified.enums.InvoiceType;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceService {

    InvoiceDto findInvoiceById(long id);
    List<InvoiceDto> getAllInvoicesOfCompany(InvoiceType invoiceType) throws Exception;
    List<InvoiceDto> getAllInvoicesByInvoiceStatus(InvoiceStatus status);
    InvoiceDto getNewInvoice(InvoiceType invoiceType) throws Exception;
    InvoiceDto save(InvoiceDto invoiceDto, InvoiceType invoiceType);
    InvoiceDto update(Long id, InvoiceDto invoiceDto);
    InvoiceDto approve(Long id);
    InvoiceDto printInvoice(Long id);
    void delete(Long id);
    List<InvoiceDto> getLastThreeInvoices();
    BigDecimal getTotalPriceOfInvoice(InvoiceDto invoiceDto);
    BigDecimal getTotalTaxOfInvoice(InvoiceDto invoiceDto);
    BigDecimal getProfitLossOfInvoice(InvoiceDto dto);
    boolean checkIfInvoiceExist(Long clientVendorId);
}

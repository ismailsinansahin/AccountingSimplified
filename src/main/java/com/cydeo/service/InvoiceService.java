package com.cydeo.service;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceService {

    InvoiceDto findInvoiceById(long id);
    List<InvoiceDto> getAllInvoicesOfCompany(InvoiceType invoiceType) throws Exception;
    List<InvoiceDto> getAllInvoicesByInvoiceStatus(InvoiceStatus status);
    InvoiceDto getNewInvoice(InvoiceType invoiceType) throws Exception;
    InvoiceDto save(InvoiceDto invoiceDto, InvoiceType invoiceType);
    InvoiceDto update(Long id, InvoiceDto invoiceDto);
    void approve(Long id);
    InvoiceDto printInvoice(Long id);
    void delete(Long id);
    List<InvoiceDto> getLastThreeInvoices();
    BigDecimal getTotalPriceOfInvoice(Long id);
    BigDecimal getTotalTaxOfInvoice(Long id);
    BigDecimal getProfitLossOfInvoice(Long id);
    boolean checkIfInvoiceExist(Long clientVendorId);
}

package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.ClientVendorDto;
import com.cydeo.accountingsimplified.dto.InvoiceDto;
import com.cydeo.accountingsimplified.dto.InvoiceProductDto;
import com.cydeo.accountingsimplified.dto.ProductDto;
import com.cydeo.accountingsimplified.enums.ClientVendorType;
import com.cydeo.accountingsimplified.enums.InvoiceType;

import java.util.List;

public interface InvoiceService {

    InvoiceDto findInvoiceById(long id);
    List<InvoiceDto> getAllInvoicesOfCompany(InvoiceType invoiceType) throws Exception;
    InvoiceDto getNewInvoice(InvoiceType invoiceType) throws Exception;
    InvoiceDto create(InvoiceDto invoiceDto, InvoiceType invoiceType);
    InvoiceDto update(Long invoiceId, InvoiceDto invoiceDto);
    InvoiceDto approve(Long invoiceId);
    void delete(Long invoiceId);

    List<InvoiceDto> getLastThreeInvoices();

}

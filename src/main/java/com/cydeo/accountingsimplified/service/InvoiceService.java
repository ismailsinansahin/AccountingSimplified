package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.InvoiceDto;
import com.cydeo.accountingsimplified.enums.InvoiceType;

import java.util.List;

public interface InvoiceService {

    InvoiceDto findInvoiceById(long id);
    List<InvoiceDto> getAllInvoicesOfCompany(InvoiceType invoiceType) throws Exception;
    InvoiceDto getNewInvoice(InvoiceType invoiceType) throws Exception;
    InvoiceDto save(InvoiceDto invoiceDto, InvoiceType invoiceType);
    InvoiceDto update(Long invoiceId, InvoiceDto invoiceDto);
    InvoiceDto approve(Long invoiceId);
    void delete(Long invoiceId);
    List<InvoiceDto> getLastThreeInvoices();

}

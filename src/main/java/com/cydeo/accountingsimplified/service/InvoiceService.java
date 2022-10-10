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
    List<ClientVendorDto> getAllClientVendorsOfCompany(ClientVendorType clientVendorType) throws Exception;
    InvoiceDto create(InvoiceDto invoiceDto, InvoiceType invoiceType);
    InvoiceDto update(Long invoiceId, InvoiceDto invoiceDto);
    InvoiceDto approve(Long invoiceId);
    void delete(Long invoiceId);
    List<ProductDto> getProductsOfCompany();

    List<InvoiceProductDto> getInvoiceProductsOfInvoice(Long invoiceId);
    InvoiceProductDto findInvoiceProductById(long id);
    void addInvoiceProduct(Long invoiceId, InvoiceProductDto invoiceProductDto);
    void removeInvoiceProduct(Long invoiceProductId);

}

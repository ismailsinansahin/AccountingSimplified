package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.InvoiceProductDto;

import java.util.List;

public interface InvoiceProductService {

    InvoiceProductDto findInvoiceProductById(long id);
    List<InvoiceProductDto> getInvoiceProductsOfInvoice(Long invoiceId);
    void addInvoiceProduct(Long invoiceId, InvoiceProductDto invoiceProductDto);
    void removeInvoiceProduct(Long invoiceProductId);

}

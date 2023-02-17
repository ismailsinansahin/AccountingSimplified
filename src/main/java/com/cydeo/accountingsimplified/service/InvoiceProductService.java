package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.InvoiceProductDto;
import com.cydeo.accountingsimplified.entity.InvoiceProduct;
import com.cydeo.accountingsimplified.entity.Product;
import com.cydeo.accountingsimplified.enums.InvoiceType;

import java.util.List;

public interface InvoiceProductService {

    InvoiceProductDto findInvoiceProductById(long id);
    List<InvoiceProductDto> getInvoiceProductsOfInvoice(Long invoiceId);
    InvoiceProductDto save(Long invoiceId, InvoiceProductDto invoiceProductDto);
    void delete(Long invoiceProductId);
    void completeApprovalProcedures(Long invoiceId, InvoiceType type);
    boolean checkProductQuantity(InvoiceProductDto salesInvoiceProduct);
    List<InvoiceProduct> findNotSoldProducts(Product product);
    List<InvoiceProduct> findAllInvoiceProductsByProductId(Long id);
}

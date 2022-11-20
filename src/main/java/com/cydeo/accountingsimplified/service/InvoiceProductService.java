package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.InvoiceProductDto;
import com.cydeo.accountingsimplified.entity.InvoiceProduct;
import com.cydeo.accountingsimplified.entity.Product;
import com.cydeo.accountingsimplified.enums.InvoiceType;

import java.util.List;

public interface InvoiceProductService {

    InvoiceProductDto findInvoiceProductById(long id);
    List<InvoiceProductDto> getInvoiceProductsOfInvoice(Long invoiceId);
    void save(Long invoiceId, InvoiceProductDto invoiceProductDto);
    void removeInvoiceProduct(Long invoiceProductId);
    int getPriceOfInvoiceProduct(Long id);
    int getTaxOfInvoiceProduct(Long id);
    int getTotalOfInvoiceProduct(Long id);
    int getProfitLossOfInvoiceProduct(Long id);
    void completeApprovalProcedures(Long invoiceId, InvoiceType type);
    boolean checkProductQuantity(InvoiceProductDto salesInvoiceProduct);
    List<InvoiceProduct> findInvoiceProductsByInvoiceTypeAndProductRemainingQuantity(InvoiceType type, Product product, Integer remainingQuantity);

}

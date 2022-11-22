package com.cydeo.accountingsimplified.repository;

import java.util.List;

import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.entity.Product;
import com.cydeo.accountingsimplified.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cydeo.accountingsimplified.entity.Invoice;
import com.cydeo.accountingsimplified.entity.InvoiceProduct;
import com.cydeo.accountingsimplified.enums.InvoiceType;

@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {

    InvoiceProduct findInvoiceProductById(Long id);
    List<InvoiceProduct> findAllByInvoice(Invoice invoice);
    List<InvoiceProduct> findAllByInvoice_Id(Long id);
    List<InvoiceProduct> findAllByInvoice_InvoiceTypeAndInvoice_Company(InvoiceType invoiceType, Company company);
    List<InvoiceProduct> findAllByInvoice_InvoiceStatusAndInvoice_Company(InvoiceStatus invoiceStatus, Company company);
    List<InvoiceProduct> findInvoiceProductsByInvoiceInvoiceTypeAndProductAndRemainingQuantityNotOrderByIdAsc(InvoiceType invoiceType, Product product, Integer remainingQuantity);


}

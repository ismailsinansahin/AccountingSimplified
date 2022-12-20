package com.cydeo.repository;

import java.util.List;

import com.cydeo.entity.Company;
import com.cydeo.entity.Product;
import com.cydeo.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cydeo.entity.Invoice;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.enums.InvoiceType;

@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {

    InvoiceProduct findInvoiceProductById(Long id);
    List<InvoiceProduct> findAllByInvoice(Invoice invoice);
    List<InvoiceProduct> findAllByInvoice_Id(Long id);
    List<InvoiceProduct> findAllByInvoice_InvoiceTypeAndInvoice_Company(InvoiceType invoiceType, Company company);
    List<InvoiceProduct> findAllByInvoice_InvoiceStatusAndInvoice_Company(InvoiceStatus invoiceStatus, Company company);
    List<InvoiceProduct> findInvoiceProductsByInvoiceInvoiceTypeAndProductAndRemainingQuantityNotOrderByIdAsc(InvoiceType invoiceType, Product product, Integer remainingQuantity);
    List<InvoiceProduct> findAllInvoiceProductByProductId(Long id);
}

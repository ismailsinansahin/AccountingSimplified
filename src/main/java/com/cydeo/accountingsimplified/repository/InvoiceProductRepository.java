package com.cydeo.accountingsimplified.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cydeo.accountingsimplified.entity.Invoice;
import com.cydeo.accountingsimplified.entity.InvoiceProduct;
import com.cydeo.accountingsimplified.enums.InvoiceType;

@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {

    InvoiceProduct findInvoiceProductById(Long id);
    List<InvoiceProduct> findInvoiceProductsByInvoice(Invoice invoice);
    List<InvoiceProduct> findInvoiceProductsByInvoiceInvoiceType(InvoiceType invoiceType);

}

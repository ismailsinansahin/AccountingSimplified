package com.cydeo.accountingsimplified.repository;

import com.cydeo.accountingsimplified.entity.Invoice;
import com.cydeo.accountingsimplified.entity.InvoiceProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {

    InvoiceProduct findInvoiceProductById(Long id);
    List<InvoiceProduct> findInvoiceProductsByInvoice(Invoice invoice);

}

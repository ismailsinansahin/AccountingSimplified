package com.cydeo.accountingsimplified.repository;

import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.entity.Invoice;
import com.cydeo.accountingsimplified.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Invoice findInvoiceById(Long id);
    List<Invoice> findInvoicesByCompanyAndInvoiceType(Company company, InvoiceType invoiceType);


}

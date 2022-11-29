package com.cydeo.accountingsimplified.repository;

import com.cydeo.accountingsimplified.entity.ClientVendor;
import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.enums.ClientVendorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientVendorRepository extends JpaRepository<ClientVendor, Long> {

    List<ClientVendor> findAllByCompany(Company company);

    List<ClientVendor> findAllByCompanyAndClientVendorType(Company company, ClientVendorType clientVendorType);

    ClientVendor findClientVendorById(Long id);

    ClientVendor findByClientVendorNameAndCompany(String companyName, Company actualCompany);
}

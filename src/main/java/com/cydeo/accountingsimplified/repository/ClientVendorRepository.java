package com.cydeo.accountingsimplified.repository;

import com.cydeo.accountingsimplified.entity.ClientVendor;
import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.enums.ClientVendorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Repository
@CrossOrigin(origins = "http://localhost:4200/")
public interface ClientVendorRepository extends JpaRepository<ClientVendor, Long> {

    List<ClientVendor> findAllByCompany(Company company);

    ClientVendor findClientVendorById(Long id);

    ClientVendor findByClientVendorNameAndCompany(String companyName, Company actualCompany);
}

package com.cydeo.accountingsimplified.repository;

import com.cydeo.accountingsimplified.entity.ClientVendor;
import com.cydeo.accountingsimplified.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientVendorRepository extends JpaRepository<ClientVendor, Long> {

    List<ClientVendor> findAllByCompany(Company company);
    ClientVendor findClientVendorById(Long id);

}

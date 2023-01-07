package com.cydeo.repository;

import com.cydeo.entity.ClientVendor;
import com.cydeo.entity.Company;
import com.cydeo.enums.ClientVendorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientVendorRepository extends JpaRepository<ClientVendor, Long> {

    List<ClientVendor> findAllByCompany(Company company);

    List<ClientVendor> findAllByCompanyAndClientVendorType(Company company, ClientVendorType clientVendorType);

    ClientVendor findByClientVendorNameAndClientVendorTypeAndCompany_Title(String ClientVendorName, ClientVendorType type,String currentCompanyTitle);
}

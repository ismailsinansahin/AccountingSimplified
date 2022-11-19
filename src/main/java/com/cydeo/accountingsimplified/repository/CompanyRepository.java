package com.cydeo.accountingsimplified.repository;

import com.cydeo.accountingsimplified.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findCompanyByTitle(String title);

    boolean existsByTitle(String title);

}

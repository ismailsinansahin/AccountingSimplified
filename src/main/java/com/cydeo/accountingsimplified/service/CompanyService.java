package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.CompanyDto;

import java.util.List;

public interface CompanyService {

    CompanyDto findCompanyById(Long id);
    CompanyDto findCompanyByTitle(String title);
    CompanyDto getCompanyByLoggedInUser();
    List<CompanyDto> getAllCompanies();
    List<CompanyDto> getFilteredCompaniesForCurrentUser();
    CompanyDto create(CompanyDto companyDto);
    CompanyDto update(Long companyId, CompanyDto companyDto) throws CloneNotSupportedException;
    CompanyDto activate(Long companyId);
    CompanyDto deactivate(Long companyId);

}

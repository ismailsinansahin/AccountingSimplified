package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.CompanyDto;
import com.cydeo.accountingsimplified.dto.UserDto;

import java.util.List;

public interface CompanyService {

    CompanyDto findCompanyById(Long id);
    List<CompanyDto> getAllCompanies();
    CompanyDto create(CompanyDto companyDto);
    CompanyDto update(Long companyId, CompanyDto companyDto);
    CompanyDto activate(Long companyId);
    CompanyDto deactivate(Long companyId);

}

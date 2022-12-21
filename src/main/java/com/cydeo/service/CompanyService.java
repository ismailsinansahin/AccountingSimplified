package com.cydeo.service;

import com.cydeo.dto.CompanyDto;

import java.util.List;

public interface CompanyService {

    CompanyDto findCompanyById(Long id);
    CompanyDto getCompanyDtoByLoggedInUser();
    List<CompanyDto> getAllCompanies();
    CompanyDto create(CompanyDto companyDto);
    CompanyDto update(Long companyId, CompanyDto companyDto) throws CloneNotSupportedException;
    CompanyDto activate(Long companyId);
    CompanyDto deactivate(Long companyId);
    boolean isTitleExist(String title);

}

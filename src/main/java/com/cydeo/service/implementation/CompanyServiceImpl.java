package com.cydeo.service.implementation;

import com.cydeo.dto.CompanyDto;
import com.cydeo.entity.Company;
import com.cydeo.enums.CompanyStatus;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.CompanyRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.SecurityService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final SecurityService securityService;
    private final MapperUtil mapperUtil;

    public CompanyServiceImpl(CompanyRepository companyRepository,
                              @Lazy SecurityService securityService, MapperUtil mapperUtil) {
        this.companyRepository = companyRepository;
        this.securityService = securityService;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public CompanyDto findCompanyById(Long id) {
        Company company = companyRepository.findById(id).get();
        return mapperUtil.convert(company, new CompanyDto());
    }

    @Override
    public List<CompanyDto> getAllCompanies() {
        boolean isRootUser = securityService.getLoggedInUser().getRole().getDescription().equalsIgnoreCase("root user");
        return companyRepository.findAll()
                .stream()
                .filter(company -> company.getId() != 1)
                .filter(each -> isRootUser ? true : each.getTitle().equals(getCompanyDtoByLoggedInUser().getTitle()))
                .sorted(Comparator.comparing(Company::getCompanyStatus).thenComparing(Company::getTitle))
                .map(each -> mapperUtil.convert(each, new CompanyDto()))
                .collect(Collectors.toList());
    }

    @Override
    public CompanyDto create(CompanyDto companyDto) {
        companyDto.setCompanyStatus(CompanyStatus.PASSIVE);
        Company company = companyRepository.save(mapperUtil.convert(companyDto, new Company()));
        return mapperUtil.convert(company, new CompanyDto());
    }

    @Override
    public CompanyDto update(Long companyId, CompanyDto companyDto) {
        Company savedCompany = companyRepository.findById(companyId).get();
        companyDto.setId(companyId);
        companyDto.setCompanyStatus(savedCompany.getCompanyStatus());
        companyDto.getAddress().setId(savedCompany.getAddress().getId());
        Company updatedCompany = mapperUtil.convert(companyDto, new Company());
        return mapperUtil.convert(companyRepository.save(updatedCompany), new CompanyDto());
    }

    @Override
    public CompanyDto activate(Long companyId) {
        Company company = companyRepository.findById(companyId).get();
        company.setCompanyStatus(CompanyStatus.ACTIVE);
        companyRepository.save(company);
        return mapperUtil.convert(company, new CompanyDto());
    }

    @Override
    public CompanyDto deactivate(Long companyId) {
        Company company = companyRepository.findById(companyId).get();
        company.setCompanyStatus(CompanyStatus.PASSIVE);
        companyRepository.save(company);
        return mapperUtil.convert(company, new CompanyDto());
    }

    @Override
    public boolean isTitleExist(String title) {
        return companyRepository.existsByTitle(title);
    }

    @Override
    public CompanyDto getCompanyDtoByLoggedInUser() {
        return securityService.getLoggedInUser().getCompany();
    }

}

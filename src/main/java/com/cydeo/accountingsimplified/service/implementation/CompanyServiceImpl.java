package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.AddressDto;
import com.cydeo.accountingsimplified.dto.CompanyDto;
import com.cydeo.accountingsimplified.entity.Address;
import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.enums.CompanyStatus;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.CompanyRepository;
import com.cydeo.accountingsimplified.service.AddressService;
import com.cydeo.accountingsimplified.service.CompanyService;
import com.cydeo.accountingsimplified.service.SecurityService;
import com.cydeo.accountingsimplified.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final AddressService addressService;
    private final SecurityService securityService;
    private final MapperUtil mapperUtil;

    public CompanyServiceImpl(CompanyRepository companyRepository, AddressService addressService,
                              @Lazy SecurityService securityService, UserService userService, MapperUtil mapperUtil) {
        this.companyRepository = companyRepository;
        this.addressService = addressService;
        this.securityService = securityService;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public CompanyDto findCompanyById(Long id) {
        Company company = companyRepository.findById(id).get();
        return mapperUtil.convert(company, new CompanyDto());
    }

    @Override
    public CompanyDto findCompanyByTitle(String title) {
        return mapperUtil.convert(companyRepository.findCompanyByTitle(title), new CompanyDto());
    }

    @Override
    public List<CompanyDto> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .filter(company -> company.getId() != 1)
                .sorted(Comparator.comparing(Company::getCompanyStatus).thenComparing(Company::getTitle))
                .map(each -> mapperUtil.convert(each, new CompanyDto()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CompanyDto> getFilteredCompaniesForCurrentUser() {
        return getAllCompanies().stream()
                .filter(each -> {
                    if (securityService.getLoggedInUser().getRole().getDescription().equalsIgnoreCase("root user")) {
//                        return companyDto.getCompanyStatus().equals(CompanyStatus.ACTIVE);
                        return true;    // if we filter by status, we have a problem to update users of passive company.
                    } else {
                        return each.getTitle().equals(getCompanyByLoggedInUser().getTitle());
                    }
                })
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
        companyRepository.save(updatedCompany);
        return mapperUtil.convert(updatedCompany, new CompanyDto());
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
    public CompanyDto getCompanyByLoggedInUser() {
        return securityService.getLoggedInUser().getCompany();
    }

}

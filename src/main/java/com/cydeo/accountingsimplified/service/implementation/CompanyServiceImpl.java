package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.AddressDto;
import com.cydeo.accountingsimplified.dto.CompanyDto;
import com.cydeo.accountingsimplified.dto.UserDto;
import com.cydeo.accountingsimplified.entity.Address;
import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.enums.CompanyStatus;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.CompanyRepository;
import com.cydeo.accountingsimplified.service.AddressService;
import com.cydeo.accountingsimplified.service.CompanyService;
import com.cydeo.accountingsimplified.service.SecurityService;
import com.cydeo.accountingsimplified.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final AddressService addressService;
    private final UserService userService;
    private final SecurityService securityService;
    private final MapperUtil mapperUtil;

    public CompanyServiceImpl(CompanyRepository companyRepository, AddressService addressService,
                              UserService userService, SecurityService securityService, MapperUtil mapperUtil) {
        this.companyRepository = companyRepository;
        this.addressService = addressService;
        this.userService = userService;
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
        return companyRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Company::getCompanyStatus))
                .map(each -> mapperUtil.convert(each, new CompanyDto()))
                .collect(Collectors.toList());
    }

    @Override
    public CompanyDto create(CompanyDto companyDto) {
        companyDto.setCompanyStatus(CompanyStatus.PASSIVE);
        addressService.save(companyDto.getAddress());
        Company company = companyRepository.save(mapperUtil.convert(companyDto, new Company()));
        return mapperUtil.convert(company, new CompanyDto());
    }

    @Override
    public CompanyDto update(Long companyId, CompanyDto companyDto) throws CloneNotSupportedException {
        Company company = companyRepository.findById(companyId).get();
        company.setTitle(companyDto.getTitle());
        company.setPhone(companyDto.getPhone());
        company.setWebsite(companyDto.getWebsite());
        AddressDto addressDto = addressService.update(company.getAddress().getId(), companyDto.getAddress());
        company.setAddress(mapperUtil.convert(addressDto, new Address()));
        companyRepository.save(company);
        return mapperUtil.convert(company, new CompanyDto());
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
    public CompanyDto getCompanyByLoggedInUser() {
        return securityService.getLoggedInUser().getCompany();
    }

}

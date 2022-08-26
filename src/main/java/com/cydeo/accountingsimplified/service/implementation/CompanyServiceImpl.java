package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.AddressDto;
import com.cydeo.accountingsimplified.dto.CompanyDto;
import com.cydeo.accountingsimplified.entity.Address;
import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.enums.CompanyStatus;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.AddressRepository;
import com.cydeo.accountingsimplified.repository.CompanyRepository;
import com.cydeo.accountingsimplified.repository.RoleRepository;
import com.cydeo.accountingsimplified.repository.UserRepository;
import com.cydeo.accountingsimplified.service.CompanyService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final AddressRepository addressRepository;
    private final MapperUtil mapperUtil;

    public CompanyServiceImpl(CompanyRepository companyRepository, AddressRepository addressRepository,
                              MapperUtil mapperUtil) {
        this.companyRepository = companyRepository;
        this.addressRepository = addressRepository;
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
        Company company = mapperUtil.convert(companyDto, new Company());
        Address address = mapperUtil.convert(companyDto.getAddress(), new Address());
        company.setAddress(address);
        companyRepository.save(company);
        return mapperUtil.convert(company, companyDto);
    }

    @Override
    public CompanyDto update(Long companyId, CompanyDto companyDto) {
        Company company = companyRepository.findById(companyId).get();
        company.setTitle(companyDto.getTitle());
        company.setPhone(companyDto.getPhone());
        company.setWebsite(companyDto.getWebsite());
        AddressDto adressDto = companyDto.getAddress();
        Address address = addressRepository.findById(company.getAddress().getId()).get();
        address.setAddressLine1(adressDto.getAddressLine1());
        address.setAddressLine2(adressDto.getAddressLine2());
        address.setCity(adressDto.getCity());
        address.setState(adressDto.getState());
        address.setCountry(adressDto.getCountry());
        address.setZipCode(adressDto.getZipCode());
        addressRepository.save(address);
        companyRepository.save(company);
        return mapperUtil.convert(company, companyDto);
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

}

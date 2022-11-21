package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.AddressDto;
import com.cydeo.accountingsimplified.dto.ClientVendorDto;
import com.cydeo.accountingsimplified.entity.Address;
import com.cydeo.accountingsimplified.entity.ClientVendor;
import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.enums.ClientVendorType;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.ClientVendorRepository;
import com.cydeo.accountingsimplified.service.AddressService;
import com.cydeo.accountingsimplified.service.ClientVendorService;
import com.cydeo.accountingsimplified.service.SecurityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientVendorServiceImpl implements ClientVendorService {

    private final ClientVendorRepository clientVendorRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;
    private final AddressService addressService;

    public ClientVendorServiceImpl(ClientVendorRepository clientVendorRepository, MapperUtil mapperUtil,
                                   SecurityService securityService, AddressService addressService) {
        this.clientVendorRepository = clientVendorRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
        this.addressService = addressService;
    }

    @Override
    public ClientVendorDto findClientVendorById(Long id) {
        ClientVendor clientVendor = clientVendorRepository.findClientVendorById(id);
        return mapperUtil.convert(clientVendor, new ClientVendorDto());
    }

    @Override
    public List<ClientVendorDto> getAllClientVendors() {
        Company company = mapperUtil.convert(securityService.getLoggedInUser().getCompany(), new Company());
        return clientVendorRepository
                .findAllByCompany(company)
                .stream()
                .map(each -> mapperUtil.convert(each, new ClientVendorDto()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ClientVendorDto> getAllClientVendorsOfCompany(ClientVendorType clientVendorType) {
        Company company = mapperUtil.convert(securityService.getLoggedInUser().getCompany(), new Company());
        return clientVendorRepository
                .findAllByCompanyAndClientVendorType(company, clientVendorType)
                .stream()
                .map(each -> mapperUtil.convert(each, new ClientVendorDto()))
                .collect(Collectors.toList());
    }

    @Override
    public ClientVendorDto create(ClientVendorDto clientVendorDto) throws Exception {
        clientVendorDto.setAddress(addressService.save(clientVendorDto.getAddress()));
        clientVendorDto.setCompany(securityService.getLoggedInUser().getCompany());
        ClientVendor clientVendor = mapperUtil.convert(clientVendorDto, new ClientVendor());
        return mapperUtil.convert(clientVendorRepository.save(clientVendor), new ClientVendorDto());
    }

    @Override
    public ClientVendorDto update(Long clientVendorId, ClientVendorDto clientVendorDto) throws ClassNotFoundException, CloneNotSupportedException {
        ClientVendor clientVendor = clientVendorRepository.findById(clientVendorId).get();
        clientVendor.setCompanyName(clientVendorDto.getCompanyName());
        clientVendor.setClientVendorType(clientVendorDto.getClientVendorType());
        clientVendor.setWebsite(clientVendorDto.getWebsite());
        clientVendor.setPhone(clientVendorDto.getPhone());
        AddressDto addressDto = addressService.update(clientVendor.getAddress().getId(), clientVendorDto.getAddress());
        clientVendor.setAddress(mapperUtil.convert(addressDto, new Address()));
        return mapperUtil.convert(clientVendorRepository.save(clientVendor), clientVendorDto);
    }

    @Override
    public void delete(Long clientVendorId) {
        ClientVendor clientVendor = clientVendorRepository.findClientVendorById(clientVendorId);
        clientVendor.setIsDeleted(true);
        clientVendorRepository.save(clientVendor);
    }

    @Override
    public boolean companyNameExists(String companyName) {
        Company actualCompany = mapperUtil.convert(securityService.getLoggedInUser().getCompany(), new Company());
        return clientVendorRepository.existsClientVendorByCompanyNameAndCompany(companyName, actualCompany);
    }

    @Override
    public boolean isCompanyNameChanged(ClientVendorDto clientVendorDto) {
        ClientVendorDto existingClientVendor = findClientVendorById(clientVendorDto.getId());
        return !existingClientVendor.getCompanyName().equals(clientVendorDto.getCompanyName());
    }


}

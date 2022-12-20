package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.ClientVendorDto;
import com.cydeo.accountingsimplified.entity.ClientVendor;
import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.enums.ClientVendorType;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.ClientVendorRepository;
import com.cydeo.accountingsimplified.service.ClientVendorService;
import com.cydeo.accountingsimplified.service.SecurityService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientVendorServiceImpl implements ClientVendorService {

    private final ClientVendorRepository clientVendorRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;

    public ClientVendorServiceImpl(ClientVendorRepository clientVendorRepository, MapperUtil mapperUtil,
                                   SecurityService securityService) {
        this.clientVendorRepository = clientVendorRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
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
                .findAllByCompany(company).stream()
                .sorted(Comparator.comparing(ClientVendor::getClientVendorType)
                .reversed()
                .thenComparing(ClientVendor::getClientVendorName))
                .map(each -> mapperUtil.convert(each, new ClientVendorDto()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ClientVendorDto> getAllClientVendors(ClientVendorType clientVendorType) {
        return getAllClientVendors()
                .stream()
                .filter(dto -> dto.getClientVendorType().equals(clientVendorType))
                .collect(Collectors.toList());
    }

    @Override
    public ClientVendorDto create(ClientVendorDto clientVendorDto) throws Exception {
        clientVendorDto.setCompany(securityService.getLoggedInUser().getCompany());
        ClientVendor clientVendor = mapperUtil.convert(clientVendorDto, new ClientVendor());
        return mapperUtil.convert(clientVendorRepository.save(clientVendor), new ClientVendorDto());
    }

    @Override
    public ClientVendorDto update(Long clientVendorId, ClientVendorDto clientVendorDto) throws ClassNotFoundException, CloneNotSupportedException {
        ClientVendor savedClientVendor = clientVendorRepository.findById(clientVendorId).get();
        clientVendorDto.getAddress().setId(savedClientVendor.getAddress().getId());     // otherwise it creates new address instead of updating existing one
        clientVendorDto.setCompany(securityService.getLoggedInUser().getCompany());
        ClientVendor updatedClientVendor = mapperUtil.convert(clientVendorDto, new ClientVendor());
        return mapperUtil.convert(clientVendorRepository.save(updatedClientVendor), new ClientVendorDto());
    }

    @Override
    public void delete(Long clientVendorId) {
        ClientVendor clientVendor = clientVendorRepository.findClientVendorById(clientVendorId);
        clientVendor.setIsDeleted(true);
        clientVendor.setClientVendorName(clientVendor.getClientVendorName() + "-" + clientVendor.getId());
        clientVendorRepository.save(clientVendor);
    }

    @Override
    public boolean companyNameExists(ClientVendorDto clientVendorDto) {
        Company actualCompany = mapperUtil.convert(securityService.getLoggedInUser().getCompany(), new Company());
        ClientVendor existingClientVendor = clientVendorRepository.findByClientVendorNameAndCompany(clientVendorDto.getClientVendorName(), actualCompany);
        if (existingClientVendor == null) return false;
        return !existingClientVendor.getId().equals(clientVendorDto.getId());
    }

}

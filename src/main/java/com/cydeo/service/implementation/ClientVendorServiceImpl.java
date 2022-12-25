package com.cydeo.service.implementation;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.entity.ClientVendor;
import com.cydeo.entity.Company;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.ClientVendorRepository;
import com.cydeo.service.ClientVendorService;
import com.cydeo.service.CompanyService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ClientVendorServiceImpl implements ClientVendorService {

    private final ClientVendorRepository clientVendorRepository;
    private final MapperUtil mapperUtil;
    private final CompanyService companyService;

    public ClientVendorServiceImpl(ClientVendorRepository clientVendorRepository, MapperUtil mapperUtil,
                                   CompanyService companyService) {
        this.clientVendorRepository = clientVendorRepository;
        this.mapperUtil = mapperUtil;
        this.companyService = companyService;
    }

    @Override
    public ClientVendorDto findClientVendorById(Long id) {
        ClientVendor clientVendor = clientVendorRepository.findById(id)
                .orElseThrow( ()-> new NoSuchElementException("This client or vendor does not exist"));
        return mapperUtil.convert(clientVendor, new ClientVendorDto());
    }

    @Override
    public List<ClientVendorDto> getAllClientVendors() {
        return clientVendorRepository
                .findAllByCompany(getCompanyOfLoggedInUsers()).stream()
                .sorted(Comparator.comparing(ClientVendor::getClientVendorType)
                .reversed()
                .thenComparing(ClientVendor::getClientVendorName))
                .map(each -> mapperUtil.convert(each, new ClientVendorDto()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ClientVendorDto> getAllClientVendors(ClientVendorType clientVendorType) {
        return clientVendorRepository
                .findAllByCompanyAndClientVendorType(getCompanyOfLoggedInUsers(), clientVendorType)
                .stream()
                .sorted(Comparator.comparing(ClientVendor::getClientVendorName))
                .map(each -> mapperUtil.convert(each, new ClientVendorDto()))
                .collect(Collectors.toList());
    }

    @Override
    public ClientVendorDto create(ClientVendorDto clientVendorDto) throws Exception {
        clientVendorDto.setCompany(companyService.getCompanyDtoByLoggedInUser());
        ClientVendor clientVendor = mapperUtil.convert(clientVendorDto, new ClientVendor());
        return mapperUtil.convert(clientVendorRepository.save(clientVendor), new ClientVendorDto());
    }

    @Override
    public ClientVendorDto update(Long clientVendorId, ClientVendorDto clientVendorDto) throws ClassNotFoundException, CloneNotSupportedException {
        ClientVendor savedClientVendor = clientVendorRepository.findById(clientVendorId)
                .orElseThrow(()-> new NoSuchElementException("This client or vendor does not exist"));
        clientVendorDto.getAddress().setId(savedClientVendor.getAddress().getId());     // otherwise it creates new address instead of updating existing one
        clientVendorDto.setCompany(companyService.getCompanyDtoByLoggedInUser());
        ClientVendor updatedClientVendor = mapperUtil.convert(clientVendorDto, new ClientVendor());
        return mapperUtil.convert(clientVendorRepository.save(updatedClientVendor), new ClientVendorDto());
    }

    @Override
    public void delete(Long clientVendorId) {
        ClientVendor clientVendor = clientVendorRepository.findById(clientVendorId)
                .orElseThrow(()-> new NoSuchElementException("This client or vendor does not exist"));
        clientVendor.setIsDeleted(true);
        clientVendor.setClientVendorName(clientVendor.getClientVendorName() + "-" + clientVendor.getId());
        clientVendorRepository.save(clientVendor);
    }

    @Override
    public boolean companyNameExists(ClientVendorDto clientVendorDto) {
        ClientVendor existingClientVendor = clientVendorRepository
                .findByClientVendorNameAndCompany(clientVendorDto.getClientVendorName(), getCompanyOfLoggedInUsers());
        if (existingClientVendor == null) return false;
        return !existingClientVendor.getId().equals(clientVendorDto.getId());
    }

    private Company getCompanyOfLoggedInUsers(){
        return mapperUtil.convert(companyService.getCompanyDtoByLoggedInUser(), new Company());
    }

}

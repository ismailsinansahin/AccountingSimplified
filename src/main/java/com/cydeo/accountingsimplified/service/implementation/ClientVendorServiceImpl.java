package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.AddressDto;
import com.cydeo.accountingsimplified.dto.ClientVendorDto;
import com.cydeo.accountingsimplified.dto.CompanyDto;
import com.cydeo.accountingsimplified.entity.Address;
import com.cydeo.accountingsimplified.entity.ClientVendor;
import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.entity.User;
import com.cydeo.accountingsimplified.entity.common.UserPrincipal;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.AddressRepository;
import com.cydeo.accountingsimplified.repository.ClientVendorRepository;
import com.cydeo.accountingsimplified.repository.UserRepository;
import com.cydeo.accountingsimplified.service.ClientVendorService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientVendorServiceImpl implements ClientVendorService {

    private final ClientVendorRepository clientVendorRepository;
    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;
    private UserPrincipal userPrincipal;
    private final AddressRepository addressRepository;

    public ClientVendorServiceImpl(ClientVendorRepository clientVendorRepository, UserRepository userRepository,
                                   MapperUtil mapperUtil, AddressRepository addressRepository) {
        this.clientVendorRepository = clientVendorRepository;
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
        this.addressRepository = addressRepository;
    }

    @Override
    public ClientVendorDto findClientVendorById(Long id) {
        ClientVendor clientVendor = clientVendorRepository.findClientVendorById(id);
        return mapperUtil.convert(clientVendor, new ClientVendorDto());
    }

    @Override
    public List<ClientVendorDto> getAllClientVendors() throws Exception {
        Company company = getCurrentUser().getCompany();
        return clientVendorRepository.findAllByCompany(company)
                .stream()
                .map(each -> mapperUtil.convert(each, new ClientVendorDto()))
                .collect(Collectors.toList());
    }

    @Override
    public ClientVendorDto create(ClientVendorDto clientVendorDto) throws Exception {
        Address address = mapperUtil.convert(clientVendorDto.getAddress(), new Address());
        addressRepository.save(address);
        CompanyDto companyDto = mapperUtil.convert(getCurrentUser().getCompany(), new CompanyDto());
        clientVendorDto.setCompany(companyDto);
        ClientVendor clientVendor = mapperUtil.convert(clientVendorDto, new ClientVendor());
        clientVendor.setAddress(address);
        clientVendorRepository.save(clientVendor);
        return mapperUtil.convert(clientVendor, clientVendorDto);
    }

    @Override
    public ClientVendorDto update(Long clientVendorId, ClientVendorDto clientVendorDto) {
        ClientVendor clientVendor = clientVendorRepository.findById(clientVendorId).get();
        clientVendor.setCompanyName(clientVendorDto.getCompanyName());
        clientVendor.setClientVendorType(clientVendorDto.getClientVendorType());
        clientVendor.setWebsite(clientVendorDto.getWebsite());
        clientVendor.setPhone(clientVendorDto.getPhone());
        AddressDto adressDto = clientVendorDto.getAddress();
        Address address = addressRepository.findById(clientVendor.getAddress().getId()).get();
        address.setAddressLine1(adressDto.getAddressLine1());
        address.setAddressLine2(adressDto.getAddressLine2());
        address.setCity(adressDto.getCity());
        address.setState(adressDto.getState());
        address.setCountry(adressDto.getCountry());
        address.setZipCode(adressDto.getZipCode());
        addressRepository.save(address);
        clientVendorRepository.save(clientVendor);
        return mapperUtil.convert(clientVendor, clientVendorDto);
    }

    @Override
    public void delete(Long clientVendorId) {
        ClientVendor clientVendor = clientVendorRepository.findClientVendorById(clientVendorId);
        clientVendor.setIsDeleted(true);
        clientVendorRepository.save(clientVendor);
    }

    public User getCurrentUser() throws Exception {
        userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findUserById(userPrincipal.getId());
    }

}

package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.ClientVendorDto;
import com.cydeo.accountingsimplified.enums.ClientVendorType;

import java.util.List;

public interface ClientVendorService {

    ClientVendorDto findClientVendorById(Long id);
    List<ClientVendorDto> getAllClientVendors() throws Exception;
    List<ClientVendorDto> getAllClientVendorsOfCompany(ClientVendorType clientVendorType);
    ClientVendorDto create(ClientVendorDto clientVendorDto) throws Exception;
    ClientVendorDto update(Long id, ClientVendorDto clientVendorDto) throws ClassNotFoundException, CloneNotSupportedException;
    void delete(Long id);

    boolean companyNameExists(String companyName);

    boolean isCompanyNameChanged(ClientVendorDto clientVendorDto);
}

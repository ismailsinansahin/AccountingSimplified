package com.cydeo.service;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.enums.ClientVendorType;

import java.util.List;

public interface ClientVendorService {

    ClientVendorDto findClientVendorById(Long id);
    List<ClientVendorDto> getAllClientVendors() throws Exception;
    List<ClientVendorDto> getAllClientVendors(ClientVendorType clientVendorType);
    ClientVendorDto create(ClientVendorDto clientVendorDto) throws Exception;
    ClientVendorDto update(Long id, ClientVendorDto clientVendorDto) throws ClassNotFoundException, CloneNotSupportedException;
    void delete(Long id);
    boolean companyNameExists(ClientVendorDto clientVendorDto);
}

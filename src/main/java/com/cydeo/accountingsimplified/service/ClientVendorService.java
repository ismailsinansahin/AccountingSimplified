package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.ClientVendorDto;

import java.util.List;

public interface ClientVendorService {

    ClientVendorDto findClientVendorById(Long id);
    List<ClientVendorDto> getAllClientVendors() throws Exception;
    ClientVendorDto create(ClientVendorDto clientVendorDto) throws Exception;
    ClientVendorDto update(Long id, ClientVendorDto clientVendorDto);
    void delete(Long id);

}

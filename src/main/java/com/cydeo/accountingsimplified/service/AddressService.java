package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.AddressDto;

public interface AddressService {

    AddressDto save(AddressDto dto);

    AddressDto getAddressById(Long id) throws ClassNotFoundException;

    AddressDto update(AddressDto dto) throws CloneNotSupportedException;
}
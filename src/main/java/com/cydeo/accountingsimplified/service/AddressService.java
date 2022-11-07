package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.AddressDto;

public interface AddressService {

    AddressDto save(AddressDto dto);
    AddressDto update(Long id, AddressDto dto) throws CloneNotSupportedException;
}

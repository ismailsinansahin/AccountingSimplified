package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.AddressDto;
import com.cydeo.accountingsimplified.entity.Address;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.AddressRepository;
import com.cydeo.accountingsimplified.service.AddressService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final MapperUtil mapper;

    public AddressServiceImpl(AddressRepository addressRepository, MapperUtil mapper) {
        this.addressRepository = addressRepository;
        this.mapper = mapper;
    }

    @Override
    public AddressDto save(AddressDto dto) {
        Address address = addressRepository.save(mapper.convert(dto, new Address()));
        return mapper.convert(address, new AddressDto());
    }

    @Override
    public AddressDto update(Long id, AddressDto dto)  {
        Address address = addressRepository.findById(id).get();
        address.setAddressLine1(dto.getAddressLine1());
        address.setAddressLine2(dto.getAddressLine2());
        address.setState(dto.getState());
        address.setCountry(dto.getCountry());
        address.setZipCode(dto.getZipCode());
        address.setCity(dto.getCity());
        return mapper.convert(addressRepository.save(address), new AddressDto());
    }
}

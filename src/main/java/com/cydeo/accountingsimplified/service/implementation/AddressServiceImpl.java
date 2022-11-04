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
    public AddressDto getAddressById(Long id) throws ClassNotFoundException {
        Optional<Address> address = addressRepository.findById(id);
        if(!address.isPresent()){
            throw new ClassNotFoundException("There is no address in db");
        }
        return mapper.convert(address, new AddressDto());
    }

    @Override
    public AddressDto update(AddressDto dto) throws CloneNotSupportedException {
        Optional<Address> address = addressRepository.findById(dto.getId());
        if(!address.isPresent()){
            throw new CloneNotSupportedException();
        }
        address.get().setAddressLine1(dto.getAddressLine1());
        address.get().setAddressLine2(dto.getAddressLine2());
        address.get().setState(dto.getState());
        address.get().setCountry(dto.getCountry());
        address.get().setZipCode(dto.getZipCode());
        address.get().setCity(dto.getCity());
        return mapper.convert(addressRepository.save(address.get()), new AddressDto());
    }
}

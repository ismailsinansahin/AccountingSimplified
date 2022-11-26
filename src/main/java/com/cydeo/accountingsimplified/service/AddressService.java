package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.AddressDto;
import com.cydeo.accountingsimplified.dto.addressApi.Country;
import com.cydeo.accountingsimplified.dto.addressApi.State;

import java.util.List;

public interface AddressService {

    List<Country> getCountryList();

    List<State> getStateList(String country);

    List<State> getCity(String state);


}

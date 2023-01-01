package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.AddressDto;
import com.cydeo.accountingsimplified.dto.addressApi.City;
import com.cydeo.accountingsimplified.dto.addressApi.Country;
import com.cydeo.accountingsimplified.dto.addressApi.State;

import java.util.List;

public interface AddressService {

    List<String> getCountryList();

    List<State> getStateList(String country);

    List<City> getCity(String state);


}

package com.cydeo.service;

import com.cydeo.dto.addressApi.State;

import java.util.List;

public interface AddressService {

    List<String> getCountryList();

    List<State> getStateList(String country);

    List<State> getCity(String state);


}

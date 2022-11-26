package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.addressApi.Country;
import com.cydeo.accountingsimplified.dto.addressApi.State;
import com.cydeo.accountingsimplified.dto.addressApi.TokenDto;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.AddressRepository;
import com.cydeo.accountingsimplified.service.feignClients.AddressFeignClient;
import com.cydeo.accountingsimplified.service.AddressService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final MapperUtil mapper;
    private final AddressFeignClient addressFeignClient;

    /**
     * These email and usertoken provided by api website after registration
     *
     * @link :https://www.universal-tutorial.com/rest-apis/free-rest-api-for-country-state-city
     * They required as req header for only get a  bearer token for future queries
     */
    @Value("${address.api.user-email}")
    private String email;

    @Value("${address.api.api-token}")
    private String userToken;

    public AddressServiceImpl(AddressRepository addressRepository, MapperUtil mapper, AddressFeignClient addressFeignClient) {
        this.addressRepository = addressRepository;
        this.mapper = mapper;
        this.addressFeignClient = addressFeignClient;
    }

    private String getBearerToken() {
        TokenDto bearerToken = addressFeignClient.auth(this.email, this.userToken);
        log.info("token retrieved for address api: " + bearerToken.getAuthToken());
        return "Bearer " + bearerToken.getAuthToken();
    }

    public List<Country> getCountryList() {

        List<Country> countriees = addressFeignClient.getCountryList(getBearerToken());

        log.info("Total Country size is :" + countriees.size());
        return countriees;

    }

    public List<State> getStateList(String country) {
        return addressFeignClient.getStateList(getBearerToken(), country);
    }

    public List<State> getCity(String state) {
        return addressFeignClient.getCityList(getBearerToken(), state);
    }
}

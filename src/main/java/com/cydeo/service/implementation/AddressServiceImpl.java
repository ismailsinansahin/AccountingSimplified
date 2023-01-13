package com.cydeo.service.implementation;

import com.cydeo.dto.common.Country;
import com.cydeo.dto.common.State;
import com.cydeo.dto.common.TokenDto;
import com.cydeo.service.feignClients.AddressFeignClient;
import com.cydeo.service.AddressService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AddressServiceImpl implements AddressService {

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

    public AddressServiceImpl(AddressFeignClient addressFeignClient) {
        this.addressFeignClient = addressFeignClient;
    }

    private String getBearerToken() {
        TokenDto bearerToken = addressFeignClient.auth(this.email, this.userToken);
        log.info("token retrieved for address api: " + bearerToken.getAuthToken());
        return "Bearer " + bearerToken.getAuthToken();
    }

    public List<String> getCountryList() {

        List<Country> countries = addressFeignClient.getCountryList(getBearerToken());

        log.info("Total Country size is :" + countries.size());
        return countries.stream()
                .map(Country::getCountryName)
                .collect(Collectors.toList());

    }

    public List<State> getStateList(String country) {
        return addressFeignClient.getStateList(getBearerToken(), country);
    }

    public List<State> getCity(String state) {
        return addressFeignClient.getCityList(getBearerToken(), state);
    }
}

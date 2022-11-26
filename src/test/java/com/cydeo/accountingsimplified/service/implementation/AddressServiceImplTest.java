package com.cydeo.accountingsimplified.service.implementation;


import com.cydeo.accountingsimplified.dto.addressApi.Country;
import com.cydeo.accountingsimplified.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@Slf4j
@ContextConfiguration
class AddressServiceImplTest {

    @Autowired
    AddressServiceImpl addressService;

    @Test
    @DisplayName("country List From Api ")
    void getCountryList() {

        var countries = addressService.getCountryList();
        Assertions.assertThat(countries).hasSizeGreaterThan(50);
        Assertions.assertThat(countries).contains("United States");
    }

    @Test
    @DisplayName("given valid country then return all states ")
    void testgetStateList() {
        var states = addressService.getStateList("United States");
        Assertions.assertThat(states).hasSizeGreaterThan(50);
    }

    @Test
    @DisplayName("given valid state then return all cities ")
    void testGetCityList() {
        var cities = addressService.getCity("Alabama");
        log.info("cities : " + cities);
        Assertions.assertThat(cities).hasSizeGreaterThan(10);
    }



}
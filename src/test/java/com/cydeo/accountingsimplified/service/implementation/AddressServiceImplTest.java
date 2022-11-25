package com.cydeo.accountingsimplified.service.implementation;


import com.cydeo.accountingsimplified.dto.addressApi.Country;
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
    @DisplayName("Country List From Api ")
    void getCountryList() {

        var countries = addressService.getCountryList();
        Assertions.assertThat(countries).hasSizeGreaterThan(50);
        Assertions.assertThat(countries).contains(
                Country.builder()
                .countryName("United States")
                .countryShortName("US")
                .countryPhoneCode(1)
                .build());
    }

    @Test
    @DisplayName("given valid country then return all states ")
    void testgetStateList() {
        var states = addressService.getStateList("United States");
        Assertions.assertThat(states).hasSizeGreaterThan(50);
    }

    @Test
    @DisplayName("given valid state then return all cities ")
    void getStateList() {
        var cities = addressService.getCity("West Virginia");
        log.info("cities : " + cities);
        Assertions.assertThat(cities).hasSizeGreaterThan(10);
    }



}
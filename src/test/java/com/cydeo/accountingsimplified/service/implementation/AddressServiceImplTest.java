package com.cydeo.accountingsimplified.service.implementation;


import com.cydeo.accountingsimplified.AccountingSimplifiedApplication;
import com.cydeo.accountingsimplified.dto.addressApi.Country;
import com.cydeo.accountingsimplified.service.AddressService;
import com.cydeo.accountingsimplified.service.feignClients.AddressFeignClient;
import feign.Feign;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest(classes = AccountingSimplifiedApplication.class)
@ActiveProfiles({"test"})
@ExtendWith(SpringExtension.class)
@Slf4j
@ContextConfiguration(classes = {AddressServiceImpl.class, AddressFeignClient.class})
class AddressServiceImplTest {


    @Autowired
    AddressService addressService;


    @Test
    @DisplayName("when fetch country List From Api response has more than 100 countries ")
    void getCountryList() {

        var countries = addressService.getCountryList();
        Assertions.assertThat(countries).hasSizeGreaterThan(100);
        Assertions.assertThat(countries).contains("United States");
    }

    @Test
    @DisplayName("given valid country then return all states")
    void testgetStateList() {
        var states = addressService.getStateList("United States");
        states.stream().forEach(e->log.info(e.getStateName()));
        Assertions.assertThat(states).hasSizeGreaterThan(50);
    }

    @Test
    @DisplayName("given valid state then return all cities")
    void testGetCityList() {
        var cities = addressService.getCity("Alabama");
        log.info("cities : " + cities);
        Assertions.assertThat(cities).hasSizeGreaterThan(10);
    }



}
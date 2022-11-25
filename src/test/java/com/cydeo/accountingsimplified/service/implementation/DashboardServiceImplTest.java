package com.cydeo.accountingsimplified.service.implementation;


import com.cydeo.accountingsimplified.dto.CurrencyDto;
import com.cydeo.accountingsimplified.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest
@ExtendWith(SpringExtension.class)
@Slf4j
class DashboardServiceImplTest {

    @Autowired
    DashboardService dashboardService;


    @Test
    @DisplayName(" Exchange rates fetching from remote api")
    void getExchangeRates() {
        var response = dashboardService.getExchangeRates();
        assertThat(response).isNotNull();
        assertThat(response).isInstanceOf(CurrencyDto.class);
        assertThat(response)
                .extracting("britishPound", "euro", "canadianDollar", "indianRupee")
                .hasSizeGreaterThan(0);


    }
}
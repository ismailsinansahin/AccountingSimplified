package com.cydeo.accountingsimplified.service.implementation;


import com.cydeo.accountingsimplified.AccountingSimplifiedApplication;
import com.cydeo.accountingsimplified.dto.CurrencyApiResponse;
import com.cydeo.accountingsimplified.dto.CurrencyDto;
import com.cydeo.accountingsimplified.service.DashboardService;
import com.cydeo.accountingsimplified.service.InvoiceService;
import com.cydeo.accountingsimplified.service.feignClients.CurrencyExchangeClient;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = AccountingSimplifiedApplication.class)
@ActiveProfiles({"test"})
class DashboardServiceImplTest extends AbstractIntegrationTest {


    @Autowired
    private DashboardServiceImpl dashboardService;




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

    @Test
    @DisplayName(" Exchange rates fetching from remote api with RESTTempalte")
    void getExchangeRateswithRestTemplate() {
        var response = dashboardService.getExchangeRatesWithRestTemplate();
        assertThat(response).isNotNull();
        assertThat(response).isInstanceOf(CurrencyDto.class);
        assertThat(response)
                .extracting("britishPound", "euro", "canadianDollar", "indianRupee")
                .hasSizeGreaterThan(0);


    }


}
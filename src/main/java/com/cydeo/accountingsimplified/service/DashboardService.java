package com.cydeo.accountingsimplified.service;


import com.cydeo.accountingsimplified.dto.CurrencyDto;

import java.math.BigDecimal;
import java.util.Map;

public interface DashboardService {

    Map<String, BigDecimal> getSummaryNumbers() throws Exception;
    CurrencyDto getExchangeRates();
}

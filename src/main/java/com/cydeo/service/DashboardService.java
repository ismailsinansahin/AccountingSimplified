package com.cydeo.service;


import com.cydeo.dto.common.CurrencyDto;

import java.math.BigDecimal;
import java.util.Map;

public interface DashboardService {

    Map<String, BigDecimal> getSummaryNumbers() throws Exception;
    CurrencyDto getExchangeRates();
}

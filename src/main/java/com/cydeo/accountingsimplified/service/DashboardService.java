package com.cydeo.accountingsimplified.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface DashboardService {

    Map<String, BigDecimal> getSummaryNumbers() throws Exception;
    List<String> getExchangeRates();

}

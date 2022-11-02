package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.CompanyDto;
import com.cydeo.accountingsimplified.dto.InvoiceDto;

import java.util.List;
import java.util.Map;

public interface DashboardService {

    Map<String, Integer> getSummaryNumbers() throws Exception;
    List<String> getExchangeRates();

}

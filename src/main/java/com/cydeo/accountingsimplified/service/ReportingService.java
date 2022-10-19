package com.cydeo.accountingsimplified.service;

import com.cydeo.accountingsimplified.dto.InvoiceProductDto;
import java.util.List;
import java.util.Map;

public interface ReportingService {

    List<InvoiceProductDto> getStockData();
    Map<String, Integer> getMonthlyProfitLossDataMap();
    
}

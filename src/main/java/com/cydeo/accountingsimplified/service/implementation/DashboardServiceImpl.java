package com.cydeo.accountingsimplified.service.implementation;


import com.cydeo.accountingsimplified.dto.CurrencyApiResponse;
import com.cydeo.accountingsimplified.dto.CurrencyDto;
import com.cydeo.accountingsimplified.dto.InvoiceDto;
import com.cydeo.accountingsimplified.entity.Product;
import com.cydeo.accountingsimplified.enums.InvoiceStatus;
import com.cydeo.accountingsimplified.enums.InvoiceType;
import com.cydeo.accountingsimplified.service.*;
import com.cydeo.accountingsimplified.service.feignClients.CurrencyExchangeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final InvoiceService invoiceService;
    private final CurrencyExchangeClient client;




    public DashboardServiceImpl(InvoiceService invoiceService, CurrencyExchangeClient client) {
        this.invoiceService = invoiceService;
        this.client = client;
    }

    @Override
    public Map<String, BigDecimal> getSummaryNumbers() {
        Map<String, BigDecimal> summaryNumbersMap = new HashMap<>();
        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal totalSales = BigDecimal.ZERO;
        BigDecimal profitLoss = BigDecimal.ZERO;
        List<InvoiceDto> allApprovedInvoicesOfCompany = invoiceService.getAllInvoicesByInvoiceStatus(InvoiceStatus.APPROVED);
        for (InvoiceDto invoice : allApprovedInvoicesOfCompany) {
            if (invoice.getInvoiceType() == InvoiceType.PURCHASE) {
                totalCost = totalCost.add(invoiceService.getTotalPriceOfInvoice(invoice.getId()));
            } else {
                totalSales = totalSales.add(invoiceService.getTotalPriceOfInvoice(invoice.getId()));
                profitLoss = profitLoss.add(invoiceService.getProfitLossOfInvoice(invoice.getId()));
            }
        }
        summaryNumbersMap.put("totalCost", totalCost);
        summaryNumbersMap.put("totalSales", totalSales);
        summaryNumbersMap.put("profitLoss", profitLoss);
        return summaryNumbersMap;
    }


    @Override
    @Cacheable(cacheNames = "currencies")
    public CurrencyDto getExchangeRates() {
        CurrencyApiResponse currency = client.getUsdBasedCurrencies();
        CurrencyDto currencyDto = CurrencyDto.builder()
                .euro(currency.getUsd().getEur())
                .britishPound(currency.getUsd().getGbp())
                .indianRupee(currency.getUsd().getInr())
                .japaneseYen(currency.getUsd().getJpy())
                .canadianDollar(currency.getUsd().getCad())
                .build();

        log.info("Currencies are fetched for the date : " + currency.getDate());

        return currencyDto;

    }

    /**
     * RestTemplate implementation for currencies fetcher
     */
    @Autowired
    RestTemplate restTemplate;

    @Value("${currency.api.url}")
    String uri;

    public CurrencyDto getExchangeRatesWithRestTemplate() {


        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<CurrencyApiResponse> entity = new HttpEntity<CurrencyApiResponse>(headers);
        var currency = restTemplate.getForObject(uri, CurrencyApiResponse.class);
        CurrencyDto currencyDto = CurrencyDto.builder()
                .euro(currency.getUsd().getEur())
                .britishPound(currency.getUsd().getGbp())
                .indianRupee(currency.getUsd().getInr())
                .japaneseYen(currency.getUsd().getJpy())
                .canadianDollar(currency.getUsd().getCad())
                .build();


        return currencyDto;

    }


//    @Autowired
//    WebClient webClient;
//    public CurrencyDto getExchangeRatesWithWebClient() {
//
//        webClient= webClient.get().uri()
//    }


}

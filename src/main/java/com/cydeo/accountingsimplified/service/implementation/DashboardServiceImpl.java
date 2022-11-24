package com.cydeo.accountingsimplified.service.implementation;


import com.cydeo.accountingsimplified.dto.CurrencyApiResponse;
import com.cydeo.accountingsimplified.dto.CurrencyDto;
import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.entity.Invoice;
import com.cydeo.accountingsimplified.enums.InvoiceStatus;
import com.cydeo.accountingsimplified.enums.InvoiceType;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.InvoiceRepository;
import com.cydeo.accountingsimplified.service.CurrencyExchangeClient;
import com.cydeo.accountingsimplified.service.DashboardService;
import com.cydeo.accountingsimplified.service.InvoiceProductService;
import com.cydeo.accountingsimplified.service.SecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceProductService invoiceProductService;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;

    private final CurrencyExchangeClient client;

    public DashboardServiceImpl(InvoiceRepository invoiceRepository, InvoiceProductService invoiceProductService,
                                MapperUtil mapperUtil, SecurityService securityService, CurrencyExchangeClient client) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceProductService = invoiceProductService;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
        this.client = client;
    }

    @Override
    public Map<String, BigDecimal> getSummaryNumbers() {
        Map<String, BigDecimal> summaryNumbersMap = new HashMap<>();
        BigDecimal totalCost = BigDecimal.ZERO;
        BigDecimal totalSales = BigDecimal.ZERO;
        BigDecimal profitLoss = BigDecimal.ZERO;
        Company company = mapperUtil.convert(securityService.getLoggedInUser().getCompany(), new Company());
        List<Invoice> allApprovedInvoicesOfCompany = invoiceRepository
                .findInvoicesByCompanyAndInvoiceStatus(company, InvoiceStatus.APPROVED);
        for (Invoice invoice : allApprovedInvoicesOfCompany) {
            if (invoice.getInvoiceType() == InvoiceType.PURCHASE) {
                totalCost = totalCost.add(invoiceProductService.getTotalOfInvoiceProduct(invoice.getId()));
            } else {
                totalSales = totalSales.add(invoiceProductService.getTotalOfInvoiceProduct(invoice.getId()));
                profitLoss = profitLoss.add(invoiceProductService.getProfitLossOfInvoiceProduct(invoice.getId()));
            }
        }
        summaryNumbersMap.put("totalCost", totalCost);
        summaryNumbersMap.put("totalSales", totalSales);
        summaryNumbersMap.put("profitLoss", profitLoss);
        return summaryNumbersMap;
    }


    @Override
    public CurrencyDto getExchangeRates() {
        CurrencyApiResponse currency = client.getUsdBasedCurrencies();
        CurrencyDto currencyDto= CurrencyDto.builder()
                .euro(currency.getUsd().getEur())
                .britishPound(currency.getUsd().getGbp())
                .indianRupee(currency.getUsd().getInr())
                .japaneseYen(currency.getUsd().getJpy())
                .canadianDollar(currency.getUsd().getCad())
                .build();

        log.info("Currencies are fetched for the date : "+ currency.getDate());

        return currencyDto;

    }


}

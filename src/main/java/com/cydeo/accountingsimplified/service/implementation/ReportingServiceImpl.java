package com.cydeo.accountingsimplified.service.implementation;

import java.util.*;
import java.util.stream.Collectors;

import com.cydeo.accountingsimplified.enums.InvoiceStatus;
import org.springframework.stereotype.Service;

import com.cydeo.accountingsimplified.dto.InvoiceProductDto;
import com.cydeo.accountingsimplified.entity.InvoiceProduct;
import com.cydeo.accountingsimplified.enums.InvoiceType;
import com.cydeo.accountingsimplified.repository.InvoiceProductRepository;
import com.cydeo.accountingsimplified.service.ReportingService;
import com.cydeo.accountingsimplified.mapper.MapperUtil;

@Service
public class ReportingServiceImpl implements ReportingService{

    private final InvoiceProductRepository invoiceProductRepository;
    private final MapperUtil mapperUtil;

    public ReportingServiceImpl(InvoiceProductRepository invoiceProductRepository, MapperUtil mapperUtil) {
        this.invoiceProductRepository = invoiceProductRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public List<InvoiceProductDto> getStockData() {
        return invoiceProductRepository.findInvoiceProductsByInvoiceInvoiceStatus(InvoiceStatus.APPROVED)
                .stream()
                .sorted(Comparator.comparing(InvoiceProduct::getId).reversed())
                .map(each -> mapperUtil.convert(each, new InvoiceProductDto()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Integer> getMonthlyProfitLossDataMap() {
        Map<String, Integer> profitLossDataMap = new TreeMap<>();
        List<InvoiceProduct> salesInvoiceProducts =invoiceProductRepository.findInvoiceProductsByInvoiceInvoiceType(InvoiceType.SALES);
        for(InvoiceProduct invoiceProduct : salesInvoiceProducts) {
            int year = invoiceProduct.getInvoice().getDate().getYear();
            String month = invoiceProduct.getInvoice().getDate().getMonth().toString();
            int profitLoss = invoiceProduct.getProfitLoss();
            String timeWindow = year + " " + month;
            profitLossDataMap.put(timeWindow, profitLossDataMap.getOrDefault(timeWindow, 0) + profitLoss);
        }
        return profitLossDataMap;
    }

}

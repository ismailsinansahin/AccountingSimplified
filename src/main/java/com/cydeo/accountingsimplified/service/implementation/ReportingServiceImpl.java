package com.cydeo.accountingsimplified.service.implementation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        return invoiceProductRepository.findAll()
        .stream()
        .map(each -> mapperUtil.convert(each, new InvoiceProductDto()))
        .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<Object>> getProfitLossDataMap() {
        Map<String, List<Object>> profitLossDataMap = new HashMap<>();
        List<InvoiceProduct> salesInvoiceProducts = invoiceProductRepository.findInvoiceProductsByInvoiceInvoiceType(InvoiceType.SALES);
        return profitLossDataMap;
    }

}

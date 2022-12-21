package com.cydeo.service.implementation;

import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.entity.Company;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.ReportingService;
import com.cydeo.service.SecurityService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class ReportingServiceImpl implements ReportingService {

    private final InvoiceProductRepository invoiceProductRepository;
    private final CompanyService companyService;
    private final MapperUtil mapperUtil;

    public ReportingServiceImpl(InvoiceProductRepository invoiceProductRepository,
                                SecurityService securityService,
                                CompanyService companyService, MapperUtil mapperUtil) {
        this.invoiceProductRepository = invoiceProductRepository;
        this.companyService = companyService;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public List<InvoiceProductDto> getStockData() {
        return invoiceProductRepository
                .findAllByInvoice_InvoiceStatusAndInvoice_Company(InvoiceStatus.APPROVED, getCompanyOfLoggedInUsers())
                .stream()
                .sorted(Comparator.comparing(InvoiceProduct::getId).reversed())
                .map(each -> mapperUtil.convert(each, new InvoiceProductDto()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, BigDecimal> getMonthlyProfitLossDataMap() {
        Map<String, BigDecimal> profitLossDataMap = new TreeMap<>();
        List<InvoiceProduct> salesInvoiceProducts = invoiceProductRepository
                .findAllByInvoice_InvoiceTypeAndInvoice_Company(InvoiceType.SALES, getCompanyOfLoggedInUsers());
        for (InvoiceProduct invoiceProduct : salesInvoiceProducts) {
            int year = invoiceProduct.getInvoice().getDate().getYear();
            String month = invoiceProduct.getInvoice().getDate().getMonth().toString();
            BigDecimal profitLoss = invoiceProduct.getProfitLoss();
            String timeWindow = year + " " + month;
            profitLossDataMap.put(timeWindow, profitLossDataMap.getOrDefault(timeWindow, BigDecimal.ZERO).add(profitLoss));
        }
        return profitLossDataMap;
    }

    private Company getCompanyOfLoggedInUsers(){
        return mapperUtil.convert(companyService.getCompanyDtoByLoggedInUser(), new Company());
    }

}

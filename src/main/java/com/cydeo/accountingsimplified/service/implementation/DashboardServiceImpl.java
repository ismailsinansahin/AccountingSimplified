package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.InvoiceDto;
import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.entity.Invoice;
import com.cydeo.accountingsimplified.enums.InvoiceStatus;
import com.cydeo.accountingsimplified.enums.InvoiceType;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.InvoiceProductRepository;
import com.cydeo.accountingsimplified.repository.InvoiceRepository;
import com.cydeo.accountingsimplified.service.CompanyService;
import com.cydeo.accountingsimplified.service.DashboardService;
import com.cydeo.accountingsimplified.service.InvoiceProductService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceProductService invoiceProductService;
    private final MapperUtil mapperUtil;
    private final CompanyService companyService;

    public DashboardServiceImpl(InvoiceRepository invoiceRepository, InvoiceProductService invoiceProductService,
                                MapperUtil mapperUtil, CompanyService companyService) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceProductService = invoiceProductService;
        this.mapperUtil = mapperUtil;
        this.companyService = companyService;
    }

    @Override
    public Map<String, Integer> getSummaryNumbers() {
        Map<String, Integer> summaryNumbersMap = new HashMap<>();
        int totalCost = 0;
        int totalSales = 0;
        int profitLoss = 0;
        Company company = mapperUtil.convert(companyService.getCompanyByLoggedInUser(), new Company());
        List<Invoice> allApprovedInvoicesOfCompany = invoiceRepository
                .findInvoicesByCompanyAndInvoiceStatus(company, InvoiceStatus.APPROVED);
        for(Invoice invoice : allApprovedInvoicesOfCompany){
            if(invoice.getInvoiceType() == InvoiceType.PURCHASE){
                totalCost += invoiceProductService.getTotalOfInvoiceProduct(invoice.getId());
            }
            else{
                totalSales += invoiceProductService.getTotalOfInvoiceProduct(invoice.getId());
                profitLoss += invoiceProductService.getProfitLossOfInvoiceProduct(invoice.getId());
            }
        }
        summaryNumbersMap.put("totalCost", totalCost);
        summaryNumbersMap.put("totalSales", totalSales);
        summaryNumbersMap.put("profitLoss", profitLoss);
        return summaryNumbersMap;
    }


    @Override
    public List<String> getExchangeRates() {
        return null;
    }


}

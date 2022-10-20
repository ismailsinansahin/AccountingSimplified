package com.cydeo.accountingsimplified.service.implementation;

import com.cydeo.accountingsimplified.dto.CompanyDto;
import com.cydeo.accountingsimplified.dto.InvoiceDto;
import com.cydeo.accountingsimplified.entity.Company;
import com.cydeo.accountingsimplified.entity.Invoice;
import com.cydeo.accountingsimplified.entity.InvoiceProduct;
import com.cydeo.accountingsimplified.entity.User;
import com.cydeo.accountingsimplified.entity.common.UserPrincipal;
import com.cydeo.accountingsimplified.enums.InvoiceStatus;
import com.cydeo.accountingsimplified.enums.InvoiceType;
import com.cydeo.accountingsimplified.mapper.MapperUtil;
import com.cydeo.accountingsimplified.repository.InvoiceProductRepository;
import com.cydeo.accountingsimplified.repository.InvoiceRepository;
import com.cydeo.accountingsimplified.repository.UserRepository;
import com.cydeo.accountingsimplified.service.DashboardService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceProductRepository invoiceProductRepository;
    private final MapperUtil mapperUtil;
    private UserPrincipal userPrincipal;

    public DashboardServiceImpl(UserRepository userRepository, InvoiceRepository invoiceRepository, InvoiceProductRepository invoiceProductRepository, MapperUtil mapperUtil) {
        this.userRepository = userRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoiceProductRepository = invoiceProductRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public CompanyDto getCurrentCompany() throws Exception {
        return mapperUtil.convert(getCurrentUser().getCompany(), new CompanyDto());
    }

    @Override
    public Map<String, Integer> getSummaryNumbers() throws Exception {
        Map<String, Integer> summaryNumbersMap = new HashMap<>();
        int totalCost = 0;
        int totalSales = 0;
        int profitLoss = 0;
        Company company = getCurrentUser().getCompany();
        List<Invoice> allApprovedInvoicesOfCompany = invoiceRepository
                .findInvoicesByCompanyAndInvoiceStatus(company, InvoiceStatus.APPROVED);
        for(Invoice invoice : allApprovedInvoicesOfCompany){
            if(invoice.getInvoiceType() == InvoiceType.PURCHASE){
                totalCost += getTotalOfInvoiceProduct(invoice.getId());
            }
            else{
                totalSales += getTotalOfInvoiceProduct(invoice.getId());
                profitLoss += getProfitLossOfInvoiceProduct(invoice.getId());
            }
        }
        summaryNumbersMap.put("totalCost", totalCost);
        summaryNumbersMap.put("totalSales", totalSales);
        summaryNumbersMap.put("profitLoss", profitLoss);
        return summaryNumbersMap;
    }

    @Override
    public List<InvoiceDto> getLastThreeInvoices() throws Exception {
        Company company = getCurrentUser().getCompany();
        List<InvoiceDto> last3Invoices = invoiceRepository.findInvoicesByCompanyAndInvoiceStatusOrderByDateDesc(company, InvoiceStatus.APPROVED)
                .stream()
                .limit(3)
                .map(each -> mapperUtil.convert(each, new InvoiceDto()))
                .collect(Collectors.toList());
        last3Invoices.forEach(each -> each.setPrice(getPriceOfInvoiceProduct(each.getId())));
        last3Invoices.forEach(each -> each.setTax(getTaxOfInvoiceProduct(each.getId())));
        last3Invoices.forEach(each -> each.setTotal(getTotalOfInvoiceProduct(each.getId())));
        return last3Invoices;
    }

    private int getPriceOfInvoiceProduct(Long id){
        Invoice invoice = invoiceRepository.findInvoiceById(id);
        List<InvoiceProduct> invoiceProductsOfInvoice = invoiceProductRepository.findInvoiceProductsByInvoice(invoice);
        return invoiceProductsOfInvoice.stream().mapToInt(InvoiceProduct::getPrice).sum();
    }

    private int getTaxOfInvoiceProduct(Long id){
        Invoice invoice = invoiceRepository.findInvoiceById(id);
        List<InvoiceProduct> invoiceProductsOfInvoice = invoiceProductRepository.findInvoiceProductsByInvoice(invoice);
        return invoiceProductsOfInvoice.stream().mapToInt(InvoiceProduct::getTax).sum();
    }

    private int getTotalOfInvoiceProduct(Long id){
        Invoice invoice = invoiceRepository.findInvoiceById(id);
        List<InvoiceProduct> invoiceProductsOfInvoice = invoiceProductRepository.findInvoiceProductsByInvoice(invoice);
        return invoiceProductsOfInvoice.stream().mapToInt(InvoiceProduct::getTotal).sum();
    }

    private int getProfitLossOfInvoiceProduct(Long id){
        Invoice invoice = invoiceRepository.findInvoiceById(id);
        List<InvoiceProduct> invoiceProductsOfInvoice = invoiceProductRepository.findInvoiceProductsByInvoice(invoice);
        return invoiceProductsOfInvoice.stream().mapToInt(InvoiceProduct::getProfitLoss).sum();
    }

    @Override
    public List<String> getExchangeRates() {
        return null;
    }

    public User getCurrentUser() throws Exception {
        userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("userPrincipal.getUsername() = " + userPrincipal.getUsername());
        return userRepository.findUserById(userPrincipal.getId());
    }

}
